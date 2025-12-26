package com.lllmtb.ui.console;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.terrain.map.Tile;
import org.jline.terminal.Terminal;

import java.util.*;

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

    private String[] cachedMapRows = null;

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

        // Subscribe to system events
        systemBus.subscribe(WorldCreatedEvent.class, this::onWorldCreated);


        // Handle terminal resize
        terminal.handle(Terminal.Signal.WINCH, sig -> {
            calculatePanelDimensions();
            cachedMapRows = null;
        });
    }

    private void onWorldCreated(WorldCreatedEvent event) {
        this.world = event.getWorld();
        cachedMapRows = null;
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

        sb.append(cachedMapRows[row]);
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
            renderEmptyPanelRow(sb);
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
            renderEmptyPanelRow(sb);
            return;
        }

        // = No panels left
        sb.append(" ".repeat(sidePanelWidth));
    }

    private void renderEmptyPanelRow(StringBuilder sb) {
        sb.append(" ".repeat(sidePanelWidth));
    }

    private String centerText(String text, int width) {
        // Strip ANSI codes for length calculation
        String stripped = text.replaceAll("\033\\[[0-9;]*m", "");
        int padding = (width - stripped.length()) / 2;
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + text + " ".repeat(width - padding - stripped.length());
    }

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}
