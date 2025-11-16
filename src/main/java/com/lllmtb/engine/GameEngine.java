package com.lllmtb.engine;

import java.awt.Window;

import com.lllmtb.ui.GraphicsUi.*;
import com.lllmtb.ui.ConsoleUI;
import com.lllmtb.ui.UI;

import java.io.IOException;

public class GameEngine {

    // public static void main(String[] args) {
    //     // Initialise the TerminalUI (or SwingUI later on)
    //     UI ui = new ConsoleUI();
    //     GameSettings settings = null;
    //     try {
    //         settings = ui.promptSettings();
    //     } catch (IOException e) {
    //         throw new RuntimeException(e);
    //     }
    //     GameWorld world = new GameWorld(settings);
    //     ui.setWorld(world);
    //     if (ui.readyToPlay()) {
    //         try {
    //             // demo run - stop after 0.5s
    //             world.start();
    //             Thread.sleep(500);
    //             world.stop();
    //         } catch (InterruptedException e) {
    //             System.out.println("Process interrupted");
    //         }
    //     }
    // }

    public static void main(String[] args) {
        UI ui = new WindowsUi();
    }
}
