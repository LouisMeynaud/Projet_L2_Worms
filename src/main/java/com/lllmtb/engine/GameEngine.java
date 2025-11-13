package com.lllmtb.engine;

import com.lllmtb.ui.TerminalUI;

public class GameEngine {

    public static void main(String[] args) {
        // Initialise the TerminalUI (or SwingUI later on)
        TerminalUI ui = new TerminalUI();
        GameSettings settings = ui.promptSettings();
        GameWorld world = new GameWorld(settings);
        if (ui.readyToPlay()) {
            try {
                // demo run - stop after 0.5s
                world.start();
                Thread.sleep(500);
                world.stop();
            } catch (InterruptedException e) {
                System.out.println("Process interrupted");
            }
        }
    }
}
