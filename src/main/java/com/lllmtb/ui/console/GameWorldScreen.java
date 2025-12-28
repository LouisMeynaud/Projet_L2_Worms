package com.lllmtb.ui.console;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.events.Event;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.*;
import com.lllmtb.events.input.ProcessInputsEvent;
import com.lllmtb.events.physics.GroundCheckCollision;
import com.lllmtb.events.turn.TurnSkipEvent;
import com.lllmtb.events.turn.TurnStartEvent;
import com.lllmtb.events.input.MoveInputEvent;
import com.lllmtb.events.input.UseInputEvent;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.team.TeamColor;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.inventory.Inventory;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.map.Tile;
import com.lllmtb.ui.graphical.GamePanel.Direction;

import org.jline.terminal.Terminal;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class GameWorldScreen implements Runnable {
    private final Terminal terminal;
    private final EventBus systemBus;
    private GameWorld world;

    // Panel dimensions (calculated on resize)
    private int termWidth;
    private int termHeight;
    private int mainPanelWidth;
    private int sidePanelWidth;
    private int infoPanelHeight;
    private int inventoryPanelHeight;
    private int logPanelHeight;

    // Viewport (camera)
    private int viewportX = 0;
    private int viewportY = 0;
    private int cachedViewportX = -1;
    private int cachedViewportY = -1;

    // State
    private volatile boolean running = true;

    // Cached map rendering (only redrawn on map updates)
    private String[] cachedMapRows = null;
    private boolean initialRenderDone = false;

    // Shooting mode state
    private boolean shootMode = false;
    private double aimAngle = 0; // in degrees: 0 = right, 90 = up
    private double aimPower = 50; // 0-100
    private static final double ANGLE_STEP = 5;
    private static final double POWER_STEP = 5;

    // Inventory selection state
    private boolean inventoryOpen = false;
    private int inventorySelection = 0;
    private final List<ItemId> inventoryItems = new ArrayList<>();

    // Key state tracking for simultaneous key presses
    private volatile boolean upPressed = false;
    private volatile boolean downPressed = false;
    private volatile boolean leftPressed = false;
    private volatile boolean rightPressed = false;

    // Event logs
    private final ConcurrentLinkedDeque<String> systemLogs = new ConcurrentLinkedDeque<>();
    private final ConcurrentLinkedDeque<String> worldLogs = new ConcurrentLinkedDeque<>();
    private static final int MAX_LOG_ENTRIES = 50;

    // Team colors
    private static final Map<TeamColor, Integer> TEAM_COLORS = Map.of(
            TeamColor.RED, 196,
            TeamColor.BLUE, 33,
            TeamColor.GREEN, 46,
            TeamColor.YELLOW, 226
    );

    // Tile colors
    private static final Map<Tile, Integer> TILE_COLORS = Map.of(
            Tile.DIRT, 94,
            Tile.GRASS, 34,
            Tile.WATER, 27,
            Tile.AIR, 0,
            Tile.CAVERN, 240,
            Tile.UNDERDIRT, 130
    );

    public GameWorldScreen(Terminal terminal, EventBus systemBus) {
        this.terminal = terminal;
        this.systemBus = systemBus;
        calculatePanelDimensions();

        // Handle gameworld creation
        systemBus.subscribe(WorldCreatedEvent.class, this::onWorldCreated);

        // Log all system bus events
        systemBus.subscribe(Event.class, event -> {
            addSystemLog(event.getClass().getSimpleName());
        });

        // Handle terminal resize
        terminal.handle(Terminal.Signal.WINCH, sig -> {
            calculatePanelDimensions();
            cachedMapRows = null;
        });
    }

    private void onWorldCreated(WorldCreatedEvent event) {
        this.world = event.getWorld();
        subscribeToWorldEvents();
        centerViewportOnCurrentWorm();
        cachedMapRows = null;
    }

    private void subscribeToWorldEvents() {
        if (world == null) return;

        EventBus worldBus = world.getBus();

        // Process movement inputs every tick
        worldBus.subscribe(ProcessInputsEvent.class, e -> processMoveInputs());

        // Center viewport on every game state update (every tick)
        worldBus.subscribe(GameStateUpdatedEvent.class, e -> {
            centerViewportOnCurrentWorm();
        });

        // Trigger full map redraw only when map is modified
        worldBus.subscribe(MapUpdateEvent.class, e -> {
            cachedMapRows = null;
        });

        // Turn events - add ammo for testing purposes
        worldBus.subscribe(TurnStartEvent.class, e -> {
            centerViewportOnCurrentWorm();
            world.getPlayingTeam().getInventory().unlock(WeaponId.GUN);
            world.getPlayingTeam().getInventory().weapons.addAmmo(WeaponId.GUN, 10);
            world.getPlayingTeam().getInventory().unlock(WeaponId.BAZOOKA);
            world.getPlayingTeam().getInventory().weapons.addAmmo(WeaponId.BAZOOKA, 3);
            shootMode = false;
            inventoryOpen = false;
        });

        // Log all world bus events
        worldBus.subscribe(Event.class, e -> {
            if (!(e instanceof GroundCheckCollision || e instanceof GameStateUpdatedEvent || e instanceof ProcessInputsEvent)) {
                addWorldLog(e.getClass().getSimpleName());
            }
        });
    }

    private void calculatePanelDimensions() {
        termWidth = terminal.getWidth();
        termHeight = terminal.getHeight();

        // 70-30
        sidePanelWidth = Math.max(25, termWidth * 30 / 100);
        mainPanelWidth = termWidth - sidePanelWidth - 1; // -1 for border

        // Side panels
        infoPanelHeight = 8;
        inventoryPanelHeight = 10;
        logPanelHeight = (termHeight - infoPanelHeight - inventoryPanelHeight - 6) / 2; // 2 log panels
    }

    private void cacheMapRows() {
        if (world == null || world.getMap() == null) {
            cachedMapRows = null;
            return;
        }

        cachedMapRows = new String[termHeight];
        Tile[][] mapMatrix = world.getMap().getMapMatrix();
        int mapHeight = world.getMap().getHeight();
        int mapWidth = world.getMap().getWidth();

        for (int row = 0; row < termHeight; row++) {
            StringBuilder rowSb = new StringBuilder();
            int mapRow = row + viewportY;

            if (mapRow < 0 || mapRow >= mapHeight) {
                rowSb.append(" ".repeat(mainPanelWidth));
            } else {
                for (int col = 0; col < mainPanelWidth; col++) {
                    int mapCol = col + viewportX;

                    if (mapCol < 0 || mapCol >= mapWidth) {
                        rowSb.append(" ");
                    } else {
                        Tile tile = mapMatrix[mapRow][mapCol];
                        int color = TILE_COLORS.getOrDefault(tile, 0);
                        char symbol = tile.getConsoleSymbol();

                        if (tile == Tile.AIR) {
                            rowSb.append(" ");
                        } else {
                            rowSb.append(String.format("\033[38;5;%dm%c\033[0m", color, symbol));
                        }
                    }
                }
            }
            if (cachedMapRows == null) return;
            cachedMapRows[row] = rowSb.toString();
        }

        // Store the viewport position this cache is valid for
        cachedViewportX = viewportX;
        cachedViewportY = viewportY;
    }

    @Override
    public void run() {
        try {
            // Enter raw mode for immediate key processing
            terminal.enterRawMode();

            // Hide cursor
            terminal.writer().print("\033[?25l");
            terminal.writer().flush();

            // Start input reader thread
            Thread inputThread = new Thread(this::readInputLoop, "console-input");
            inputThread.setDaemon(true);
            inputThread.start();

            // Main render loop
            while (running) {
                if (!initialRenderDone) {
                    renderFull();
                    initialRenderDone = true;
                } else {
                    renderPartial();
                }

                Thread.sleep(8); // cap tick rate (~125 TPS)
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // Show cursor
            terminal.writer().print("\033[?25h");
            terminal.writer().flush();
        }
    }

    private void readInputLoop() {
        try {
            while (running) {
                int ch = terminal.reader().read(50); // 50ms timeout
                if (ch == -2) continue; // timeout
                if (ch == -1) {
                    running = false;
                    break;
                }
                handleKeyPress(ch);
            }
        } catch (IOException e) {
            Logger.logger.error("Error reading terminal input", e);
        }
    }

    private void handleKeyPress(int ch) {
        if (world == null) return;

        // escape sequences for arrow keys
        if (ch == 27) { // ESC
            try {
                int next = terminal.reader().read(50);
                if (next == '[') {
                    int arrow = terminal.reader().read(50);
                    switch (arrow) {
                        case 'A' -> handleArrowUp();
                        case 'B' -> handleArrowDown();
                        case 'C' -> handleArrowRight();
                        case 'D' -> handleArrowLeft();
                        default -> {
                        }
                    }
                } else if (next == -2) { // Just ESC
                    if (shootMode) shootMode = false;
                    else if (inventoryOpen) inventoryOpen = false;
                }
            } catch (IOException e) {
                Logger.logger.error("Error reading escape sequence", e);
            }
            return;
        }

        switch (ch) {
            // Shoot mode toggle
            case ' ' -> { // Space
                if (inventoryOpen) selectInventoryItem();
                else shootMode = !shootMode;
            }

            // Fire in shoot mode
            case '\r', '\n' -> { // Enter
                if (inventoryOpen) selectInventoryItem();
                else if (shootMode) fireWeapon();
            }

            // Inventory toggle
            case 'i', 'I' -> {
                inventoryOpen = !inventoryOpen;
                if (inventoryOpen) {
                    buildInventoryList();
                    shootMode = false;
                }
            }

            // Skip turn
            case 't', 'T' -> world.getBus().postQueued(new TurnSkipEvent());

            // Quit
            case 'q', 'Q' -> {
                running = false;
            }

            // Number keys for quick weapon selection
            case '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                int index = ch - '1';
                if (inventoryOpen && index < inventoryItems.size()) {
                    inventorySelection = index;
                    selectInventoryItem();
                }
            }

            default -> {
            }
        }
    }

    private void handleArrowUp() {
        if (shootMode) aimAngle = Math.min(225, aimAngle + ANGLE_STEP);
        else if (inventoryOpen) inventorySelection = Math.max(0, inventorySelection - 1);
        else upPressed = true;
    }

    private void handleArrowDown() {
        if (shootMode) aimAngle = Math.max(-45, aimAngle - ANGLE_STEP);
        else if (inventoryOpen) inventorySelection = Math.min(inventoryItems.size() - 1, inventorySelection + 1);
        else downPressed = true;
    }

    private void handleArrowLeft() {
        if (shootMode) aimPower = Math.max(0, aimPower - POWER_STEP);
        else if (!inventoryOpen) leftPressed = true;
        // In inventory mode, left arrow does nothing
    }

    private void handleArrowRight() {
        if (shootMode) aimPower = Math.min(100, aimPower + POWER_STEP);
        else if (!inventoryOpen) rightPressed = true;
        // In inventory mode, right arrow does nothing
    }

    /**
     * Process movement inputs based on currently pressed keys.
     *  /\ Note that due to terminal limitations, it is not possible to tell when a key is being held after a combo.
     *  That means it is impossible to have smooth diagonal movement by holding two arrow keys at once.
     */
    private void processMoveInputs() {
        if (world == null || shootMode || inventoryOpen) return;

        if (upPressed) {
            world.getBus().postQueued(new MoveInputEvent(Direction.Up));
            upPressed = false;
        }
        if (downPressed) {
            world.getBus().postQueued(new MoveInputEvent(Direction.Down));
            downPressed = false;
        }
        if (leftPressed) {
            world.getBus().postQueued(new MoveInputEvent(Direction.Left));
            leftPressed = false;
        }
        if (rightPressed) {
            world.getBus().postQueued(new MoveInputEvent(Direction.Right));
            rightPressed = false;
        }
    }

    private void fireWeapon() {
        if (world == null) return;

        Team team = world.getPlayingTeam();
        if (team == null) return;

        Worm worm = team.getCurrentWorm();
        if (worm == null) return;

        // Create a target position in front of the worm (i.e. mock the mouse click)
        double angleRad = Math.toRadians(aimAngle);
        Position wormPos = worm.getBody().getPos();
        double distance = 5;
        Position targetPos = new Position(
                wormPos.getX() + Math.cos(angleRad) * distance * worm.getFacingDirection(),
                wormPos.getY() + Math.sin(angleRad) * distance
        );

        world.getBus().postQueued(new UseInputEvent(targetPos, aimPower * 10));
    }

    private void buildInventoryList() {
        inventoryItems.clear();
        if (world == null || world.getPlayingTeam() == null) return;

        inventoryItems.addAll(world.getPlayingTeam().getInventory().weapons.ammoSnapshot().keySet());
        inventoryItems.addAll(world.getPlayingTeam().getInventory().tools.ammoSnapshot().keySet());

        inventorySelection = Math.min(inventorySelection, Math.max(0, inventoryItems.size() - 1));
    }

    private void selectInventoryItem() {
        if (inventoryItems.isEmpty() || world == null) return;

        ItemId selected = inventoryItems.get(inventorySelection);
        if (selected instanceof WeaponId weaponId) world.getBus().postQueued(new WeaponSelectedEvent(weaponId));
        else if (selected instanceof ToolId toolId) world.getBus().postQueued(new ToolSelectedEvent(toolId));

        inventoryOpen = false;
        shootMode = true;
    }

    private void centerViewportOnCurrentWorm() {
        if (world == null) return;
        Team team = world.getPlayingTeam();
        if (team == null) return;
        Worm worm = team.getCurrentWorm();
        if (worm == null) return;

        Position pos = worm.getBody().getPos();
        int mapHeight = world.getMap().getHeight();

        // Center viewport on worm
        viewportX = (int) pos.getX() - (mainPanelWidth - 2) / 2;
        viewportY = (int) (mapHeight - pos.getY()) - (termHeight - 2) / 2;

        // Clamp viewport
        viewportX = Math.max(0, Math.min(viewportX, world.getMap().getWidth() - mainPanelWidth + 2));
        viewportY = Math.max(0, Math.min(viewportY, mapHeight - termHeight + 2));
    }

    private void renderFull() {
        StringBuilder sb = new StringBuilder();

        // Clear screen and move cursor to top-left
        sb.append("\033[2J\033[H");

        // Rebuild and cache map rows
        cacheMapRows();

        // Render each row
        for (int row = 0; row < termHeight; row++) {
            renderFullRow(sb, row);
            if (row < termHeight - 1) sb.append("\n");
        }

        terminal.writer().print(sb);
        terminal.writer().flush();
    }

    private void renderPartial() {
        StringBuilder sb = new StringBuilder();

        // Re-render the main panel
        for (int row = 0; row < termHeight; row++) {
            sb.append(String.format("\033[%d;1H", row + 1));
            renderMainPanelRow(sb, row);
            sb.append("\033[90m│\033[0m");
            renderSidePanelRow(sb, row);
        }

        terminal.writer().print(sb);
        terminal.writer().flush();
    }

    private void renderFullRow(StringBuilder sb, int row) {
        // Main panel content (game world)
        renderMainPanelRow(sb, row);

        // Vertical separator
        sb.append("\033[90m│\033[0m");

        // Side panel content (info, inventory, logs)
        renderSidePanelRow(sb, row);
    }

    private void renderMainPanelRow(StringBuilder sb, int row) {
        if (world == null || world.getMap() == null) {
            String msg = row == termHeight / 2 ? centerText("Waiting for game to start...", mainPanelWidth) : " ".repeat(mainPanelWidth);
            sb.append(msg);
            return;
        }

        // If viewport changed, invalidate cache
        if (cachedMapRows != null && (cachedViewportX != viewportX || cachedViewportY != viewportY)) {
            cachedMapRows = null;
        }

        // If no cache, build it
        if (cachedMapRows == null) {
            cacheMapRows();
        }

        if (cachedMapRows == null || row >= cachedMapRows.length) {
            sb.append(" ".repeat(mainPanelWidth));
            return;
        }

        int mapHeight = world.getMap().getHeight();
        int mapRow = row + viewportY;

        if (mapRow < 0 || mapRow >= mapHeight) {
            sb.append(" ".repeat(mainPanelWidth));
            return;
        }

        // Get the entities on this row
        Map<Integer, EntityRender> entityPositions = getEntityPositionsForRow(mapHeight - 1 - mapRow);

        if (entityPositions.isEmpty()) {
            // No entities, use cached row directly
            sb.append(cachedMapRows[row]);
        } else {
            // Overlay entities on top of the cached map
            Tile[][] mapMatrix = world.getMap().getMapMatrix();
            int mapWidth = world.getMap().getWidth();

            for (int col = 0; col < mainPanelWidth; col++) {
                int mapCol = col + viewportX;

                if (mapCol < 0 || mapCol >= mapWidth) {
                    sb.append(" ");
                    continue;
                }

                if (entityPositions.containsKey(mapCol)) {
                    EntityRender er = entityPositions.get(mapCol);
                    sb.append(String.format("\033[38;5;%dm%c\033[0m", er.color, er.symbol));
                } else {
                    Tile tile = mapMatrix[mapRow][mapCol];
                    int color = TILE_COLORS.getOrDefault(tile, 0);
                    char symbol = tile.getConsoleSymbol();
                    sb.append(tile == Tile.AIR ? " " : String.format("\033[38;5;%dm%c\033[0m", color, symbol));
                }
            }
        }
    }

    private record EntityRender(char symbol, int color) {
    }

    private Map<Integer, EntityRender> getEntityPositionsForRow(int mapY) {
        Map<Integer, EntityRender> positions = new HashMap<>();
        if (world == null) return positions;

        for (PhysicsBody body : world.getPhysics().getBodies()) {
            if (body == null || body.getEntity() == null) continue;
            Position pos = body.getPos();

            // (x, y) = bottom left corner
            int baseX = (int) Math.round(pos.getX());
            int baseY = (int) Math.round(pos.getY());
            int bodyWidth = (int) Math.ceil(body.getWidth());
            int bodyHeight = (int) Math.ceil(body.getHeight());

            // a single entity might have to be rendered on multiple rows/columns (e.g. a worm is 1x2)
            if (mapY >= baseY && mapY < baseY + bodyHeight) {
                int color;
                char symbol;

                if (body.getEntity() instanceof Worm worm) {
                    TeamColor tc = worm.getTeam().teamColor;
                    color = TEAM_COLORS.getOrDefault(tc, 255);
                    symbol = '@';
                } else if (body.getEntity() instanceof Projectile proj) {
                    TeamColor tc = proj.getOwner().getTeam().teamColor;
                    color = TEAM_COLORS.getOrDefault(tc, 255);
                    symbol = '*';
                } else {
                    continue;
                }

                for (int dx = 0; dx < bodyWidth; dx++) {
                    positions.put(baseX + dx, new EntityRender(symbol, color));
                }
            }
        }

        return positions;
    }

    private void renderSidePanelRow(StringBuilder sb, int row) {
        int currentRow = 0;

        // Info panel
        if (row < infoPanelHeight) {
            renderInfoPanelRow(sb, row);
            return;
        }
        currentRow += infoPanelHeight;

        // Border after info panel
        if (row == currentRow) {
            sb.append("\033[90m").append("─".repeat(sidePanelWidth)).append("\033[0m");
            return;
        }
        currentRow++;

        // Inventory panel
        if (row < currentRow + inventoryPanelHeight) {
            renderInventoryPanelRow(sb, row - currentRow);
            return;
        }
        currentRow += inventoryPanelHeight;

        // Border after inventory panel
        if (row == currentRow) {
            sb.append("\033[90m").append("─".repeat(sidePanelWidth)).append("\033[0m");
            return;
        }
        currentRow++;

        // System log panel
        if (row < currentRow + logPanelHeight) {
            renderLogPanelRow(sb, row - currentRow, "System Events", systemLogs);
            return;
        }
        currentRow += logPanelHeight;

        // Border between log panels
        if (row == currentRow) {
            sb.append("\033[90m").append("─".repeat(sidePanelWidth)).append("\033[0m");
            return;
        }
        currentRow++;

        // World log panel
        if (row < currentRow + logPanelHeight) {
            renderLogPanelRow(sb, row - currentRow, "World Events", worldLogs);
            return;
        }

        // = No panels left
        sb.append(" ".repeat(sidePanelWidth));
    }

    private void renderInfoPanelRow(StringBuilder sb, int row) {
        String line = "";

        switch (row) {
            case 0 -> {
                line = "\033[1m" + centerText("=== INFO ===", sidePanelWidth) + "\033[0m";
            }
            case 1 -> {
                if (world != null && world.getPlayingTeam() != null) {
                    Team team = world.getPlayingTeam();
                    int color = TEAM_COLORS.getOrDefault(team.teamColor, 255);
                    line = String.format(" Team: \033[38;5;%dm%s\033[0m", color, team.name);
                } else {
                    line = " Team: ---";
                }
            }
            case 2 -> {
                if (world != null && world.getPlayingTeam() != null) {
                    Worm worm = world.getPlayingTeam().getCurrentWorm();
                    if (worm != null) {
                        line = String.format(" Worm: %s (HP: %d)", worm.name, worm.getStats().getHp());
                    }
                } else {
                    line = " Worm: ---";
                }
            }
            case 3 -> {
                if (world != null && world.getTurnManager() != null) {
                    long time = world.getTurnManager().getRemainingTime() / 1000;
                    String timeStr = String.format("%02d:%02d", time / 60, time % 60);
                    String color = time < 5 ? "\033[31m" : "\033[32m";
                    line = String.format(" Time: %s%s\033[0m", color, timeStr);
                } else {
                    line = " Time: --:--";
                }
            }
            case 4 -> {
                line = "";
            }
            case 5 -> {
                if (shootMode) line = String.format(" \033[33m[SHOOT MODE]\033[0m Angle: %.0f°", aimAngle);
                else if (inventoryOpen) line = " \033[36m[INVENTORY]\033[0m";
                else line = " \033[32m[MOVE MODE]\033[0m";
            }
            case 6 -> {
                if (shootMode) line = String.format(" Power: %.0f%% [◄/►]", aimPower);
                else line = " [I]nventory [Space]Shoot";
            }
            case 7 -> {
                if (shootMode) line = " [Enter]Fire [Esc]Cancel";
                else line = " [T]Skip turn [Q]Quit";
            }
            default -> {
            }
        }

        sb.append(padRight(line, sidePanelWidth));
    }

    private void renderInventoryPanelRow(StringBuilder sb, int row) {
        if (row == 0) {
            sb.append("\033[1m").append(centerText("=== INVENTORY ===", sidePanelWidth)).append("\033[0m");
            return;
        }

        if (!inventoryOpen || world == null || world.getPlayingTeam() == null) {
            if (row == 1) sb.append(padRight(" Press [I] to open", sidePanelWidth));
            else sb.append(" ".repeat(sidePanelWidth));
            return;
        }

        int itemRow = row - 1;
        if (itemRow < inventoryItems.size()) {
            Object item = inventoryItems.get(itemRow);
            Inventory inv = world.getPlayingTeam().getInventory();

            String name;
            int ammo;
            boolean available;

            if (item instanceof WeaponId wid) {
                name = wid.name();
                ammo = inv.weapons.getAmmo(wid);
                available = inv.canFire(wid);
            } else if (item instanceof ToolId tid) {
                name = tid.name();
                ammo = inv.tools.getAmmo(tid);
                available = inv.canUse(tid);
            } else {
                name = "???";
                ammo = 0;
                available = false;
            }

            String prefix;
            String suffix = "\033[0m";
            if (itemRow == inventorySelection) prefix = "\033[7m>"; // Inverted colours for selection
            else if (!available) prefix = "\033[90m "; // Gray for unavailable
            else {
                prefix = " ";
                suffix = "";
            }

            String ammoStr = ammo > 0 ? String.format("x%d", ammo) : "---";
            String line = String.format("%s%d. %s (%s)%s", prefix, itemRow + 1, name, ammoStr, suffix);
            sb.append(padRight(line, sidePanelWidth));
        } else {
            sb.append(" ".repeat(sidePanelWidth));
        }
    }

    private void renderLogPanelRow(StringBuilder sb, int row, String title, Deque<String> logs) {
        if (row == 0) {
            sb.append("\033[1m").append(centerText("=== " + title + " ===", sidePanelWidth)).append("\033[0m");
            return;
        }

        int logIndex = row - 1;
        List<String> logList = new ArrayList<>(logs);
        Collections.reverse(logList); // Most recent first

        if (logIndex < logList.size()) {
            String entry = logList.get(logIndex);
            if (entry.length() > sidePanelWidth - 1) {
                entry = entry.substring(0, sidePanelWidth - 4) + "...";
            }
            sb.append(padRight(" " + entry, sidePanelWidth));
        } else {
            sb.append(" ".repeat(sidePanelWidth));
        }
    }

    private void addSystemLog(String message) {
        while (systemLogs.size() >= MAX_LOG_ENTRIES) {
            systemLogs.pollFirst();
        }
        systemLogs.addLast(message);
    }

    private void addWorldLog(String message) {
        while (worldLogs.size() >= MAX_LOG_ENTRIES) {
            worldLogs.pollFirst();
        }
        worldLogs.addLast(message);
    }

    private String centerText(String text, int width) {
        // Strip ANSI codes for length calculation
        String stripped = text.replaceAll("\033\\[[0-9;]*m", "");
        int padding = (width - stripped.length()) / 2;
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + text + " ".repeat(width - padding - stripped.length());
    }

    private String padRight(String text, int width) {
        // Strip ANSI codes for length calculation
        String stripped = text.replaceAll("\033\\[[0-9;]*m", "");
        int padding = width - stripped.length();
        if (padding < 0) return text.substring(0, width); // truncate
        return text + " ".repeat(padding);
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
