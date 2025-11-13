package com.lllmtb.engine;

import com.lllmtb.ui.TerminalUI;

public class GameEngine {

    public static void main(String[] args) {
        // With default values until we can prompt the user via the UI
        // Initialise the Terminal UI
        TerminalUI ui = new TerminalUI();
        GameSettings settings = ui.promptSettings();
        GameWorld world = new GameWorld(settings);
        if (ui.readyToPlay()) {
            world.start();
        }

//        String[] teamNames = {"Blue", "Red"};
//        GameSettings settings = new GameSettings(2, teamNames ,3,false, "testMap", false);
//        System.out.println("Generated game settings");
//        GameWorld world = new GameWorld(settings);
//        world.start();
    }
}
