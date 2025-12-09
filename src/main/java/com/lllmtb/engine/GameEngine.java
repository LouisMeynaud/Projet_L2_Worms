package com.lllmtb.engine;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.GamePauseEvent;
import com.lllmtb.events.game.StartGameEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.ui.ConsoleUI;
import com.lllmtb.ui.UI;
import com.lllmtb.ui.graphical.GraphicalUI;
import com.lllmtb.ui.graphical.TextureManager;

public class GameEngine {
    private static EventBus systemBus;
    private static GameWorld world;

    public static void main(String[] args) {
        boolean graphique = true;
        systemBus = new EventBus();
        systemBus.subscribe(StartGameEvent.class, GameEngine::startGame);

        // Engine loop thread - processes engine-level events (start, pause, quit...)
        new Thread(() -> {
            Thread current = Thread.currentThread();
            while (!current.isInterrupted()) {
                systemBus.processQueued();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    current.interrupt();
                    break;
                }
            }
        }, "engine-loop").start();


        // Start the chosen UI manager
        UI ui;
        if (graphique) {
            TextureManager.load(); // charge les textures
            ui = new GraphicalUI(systemBus);
        } else {
            ui = new ConsoleUI(systemBus);
        }
        ui.start();
    }

    /**
     * Handles the StartGameEvent to initialize and start the game world.
     * @param event the StartGameEvent containing game settings
     */
    private static void startGame(@NonNull StartGameEvent event) {
        GameSettings settings = Objects.requireNonNull(event).getSettings();
        EventBus worldBus = new EventBus();
        world = new GameWorld(settings, worldBus);
        systemBus.postSync(new WorldCreatedEvent(world));
        world.start();
        systemBus.subscribe(GamePauseEvent.class, e -> world.pause(e.isPaused));
    }
}