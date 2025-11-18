package com.lllmtb.engine;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.TheEvents.StartGameEvent;
import com.lllmtb.events.TheEvents.WorldCreatedEvent;
import com.lllmtb.ui.ConsoleUI;
import com.lllmtb.ui.UI;
import com.lllmtb.ui.GraphicsUi.WindowsUi;

public class GameEngine {


    private static EventBus eventBus;
    private static GameWorld world;
    private static volatile GameLoop gameLoop;

    

    public static void main(String[] args) {
        
        boolean graphique = true;
        eventBus = new EventBus();


        eventBus.subscribe(StartGameEvent.class, event -> {
            startGame(event);
        });

        new Thread(() -> {
            while (gameLoop == null) {
                eventBus.processQueued(); 
                try { Thread.sleep(10); } catch (InterruptedException e) {}
            }
        }, "Engine-Monitor").start();

    
        UI ui;
        if (graphique) {
            ui = new WindowsUi(eventBus);
        } else {
            ui = new ConsoleUI(eventBus);
        }
        ui.start(); 
    }

    private static void startGame(StartGameEvent event) {
        GameSettings settings = event.getSettings(); 
        world = new GameWorld(settings, eventBus); 
        eventBus.postQueued(new WorldCreatedEvent(world));
        gameLoop = new GameLoop(world); 
        new Thread(gameLoop, "GameLoop-Thread").start(); 
    }
}