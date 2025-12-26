package com.lllmtb.ui.console;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
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

    public GameWorldScreen(Terminal terminal, EventBus systemBus) {
        this.terminal = terminal;
        this.systemBus = systemBus;

        // Handle terminal resize
        terminal.handle(Terminal.Signal.WINCH, sig -> {
            calculatePanelDimensions();
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
        renderEmptyPanelRow(sb);
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

    public void stop() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }
}