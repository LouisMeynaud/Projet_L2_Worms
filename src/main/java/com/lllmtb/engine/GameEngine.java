package com.lllmtb.engine;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.StartGameEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.ui.ConsoleUI;
import com.lllmtb.ui.UI;
import com.lllmtb.ui.graphical.GraphicalUI;
import com.lllmtb.ui.graphical.TextureManager;

import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

public class GameEngine {
    private static EventBus eventBus;
    private static GameWorld world;

    public static void main(String[] args) {
        boolean graphique = true;
        eventBus = new EventBus();
        eventBus.subscribe(StartGameEvent.class, GameEngine::startGame);

        // Engine loop thread - processes events until the world is created and the GameLoop takes over
        new Thread(() -> {
            while (world == null) {
                eventBus.processQueued();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        }, "engine-loop").start();


        // Start the chosen UI manager
        UI ui;
        if (graphique) {
            TextureManager.load(); // charge les textures
            ui = new GraphicalUI(eventBus);
        } else {
            ui = new ConsoleUI(eventBus);
        }
        ui.start();
    }

    /**
     * Handles the StartGameEvent to initialize and start the game world.
     * @param event the StartGameEvent containing game settings
     */
    private static void startGame(@NonNull StartGameEvent event) {
        GameSettings settings = Objects.requireNonNull(event).getSettings();
        world = new GameWorld(settings, eventBus);
        eventBus.postSync(new WorldCreatedEvent(world));
        world.start();
    }
}