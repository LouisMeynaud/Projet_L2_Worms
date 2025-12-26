package com.lllmtb.ui.console;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.Event;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.events.game.MapUpdateEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.events.input.ProcessInputsEvent;
import com.lllmtb.events.physics.GroundCheckCollision;
import com.lllmtb.events.physics.PhysicsCollisionWithMap;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.TeamColor;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.map.Tile;
import org.jline.terminal.Terminal;

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

    private volatile boolean running = true;

    // Cached map rendering (only redrawn on map updates)
    private String[] cachedMapRows = null;

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
        cachedMapRows = null;
    }

    private void subscribeToWorldEvents() {
        if (world == null) return;

        EventBus worldBus = world.getBus();

        // Trigger map redraw
        worldBus.subscribe(MapUpdateEvent.class, e -> {
            cachedMapRows = null;
        });

        // Log all world bus events
        worldBus.subscribe(Event.class, e -> {
            if (!(e instanceof GroundCheckCollision || e instanceof PhysicsCollisionWithMap || e instanceof GameStateUpdatedEvent || e instanceof ProcessInputsEvent))
                addWorldLog(e.getClass().getSimpleName());
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
            int mapRow = row;

            if (mapRow < 0 || mapRow >= mapHeight) {
                rowSb.append(" ".repeat(mainPanelWidth));
            } else {
                for (int col = 0; col < mainPanelWidth; col++) {
                    int mapCol = col;

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
            cachedMapRows[row] = rowSb.toString();
        }
    }

    @Override
    public void run() {
        try {
            // Hide cursor
            terminal.writer().print("\033[?25l");
            terminal.writer().flush();

            // Main render loop
            while (running) {
                renderFull();
            }
        } finally {
            // Show cursor
            terminal.writer().print("\033[?25h");
            terminal.writer().flush();
        }
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

        // If no cache, build it
        if (cachedMapRows == null) {
            cacheMapRows();
        }

        if (cachedMapRows == null || row >= cachedMapRows.length) {
            sb.append(" ".repeat(mainPanelWidth));
            return;
        }

        int mapHeight = world.getMap().getHeight();
        int mapRow = row;

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
            // Overlay entities on the cached map
            Tile[][] mapMatrix = world.getMap().getMapMatrix();
            int mapWidth = world.getMap().getWidth();

            for (int col = 0; col < mainPanelWidth; col++) {
                int mapCol = col;

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

    private record EntityRender(char symbol, int color) {}

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
            renderEmptyPanelRow(sb);
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
            renderEmptyPanelRow(sb);
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
            renderLogPanelRow(sb, row, "System Events", systemLogs);
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
            renderLogPanelRow(sb, row, "World Events", worldLogs);
            return;
        }

        // = No panels left
        sb.append(" ".repeat(sidePanelWidth));
    }

    private void renderEmptyPanelRow(StringBuilder sb) {
        sb.append(" ".repeat(sidePanelWidth));
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
