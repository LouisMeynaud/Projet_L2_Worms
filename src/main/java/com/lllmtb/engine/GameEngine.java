package com.lllmtb.engine;

import java.io.IOException;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.engine.save.Save;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.GamePauseEvent;
import com.lllmtb.events.game.SaveEvent;
import com.lllmtb.events.game.StartGameEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.ui.ConsoleUI;
import com.lllmtb.ui.UI;
import com.lllmtb.ui.graphical.GraphicalUI;
import com.lllmtb.ui.graphical.SoundManager;
import com.lllmtb.ui.graphical.TextureManager;
import com.lllmtb.ui.graphical.animation.AnimationManager;

public class GameEngine {
    private static EventBus systemBus;
    private static GameWorld world;

    public static void main(String[] args) {
        boolean graphique = true;
        systemBus = new EventBus();
        // Uncomment to log all system events for debugging
        //systemBus.subscribe(Event.class, e -> Logger.logger.info("[EVENT/system]: {}", e.getClass().getSimpleName()));
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
            Logger.logger.info("Using graphical interface");
            TextureManager.load(); // load textures
            AnimationManager.load(); //load animation
            SoundManager.load(); //load sound effect
            ui = new GraphicalUI(systemBus);
        } else {
            Logger.logger.info("Using console interface");
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
        // Uncomment to log all world events for debugging
        //worldBus.subscribe(Event.class, e -> Logger.logger.info("[EVENT/world]: {}", e.getClass().getSimpleName()));
        world = new GameWorld(settings, worldBus);
        Save.setGameWorld(world);
        systemBus.postSync(new WorldCreatedEvent(world));
        systemBus.subscribe(SaveEvent.class, e -> {
            try {
                Save.saveAll();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        world.start();
        systemBus.subscribe(GamePauseEvent.class, e -> world.pause(e.isPaused));
    }
}