package com.lllmtb.engine;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.TheEvents.StartGameEvent;
import com.lllmtb.events.TheEvents.WorldCreatedEvent;



public class GameEngine {

    
    private final EventBus eventBus;
    private GameWorld world;
    private GameLoop gameLoop;
    private volatile boolean monitoring = true;

    
    public GameEngine(EventBus bus) {
        this.eventBus = bus;
        subscribeToEvents(); 

        new Thread(this::monitorBusForStart, "Engine-Monitor").start();
    }

    private void subscribeToEvents() {
        eventBus.subscribe(StartGameEvent.class, event -> {
            handleGameStart(event);
        });
    }

    
    private void handleGameStart(StartGameEvent event) {
        GameSettings settings = event.getSettings(); //on recup les setting
        

        this.world = new GameWorld(settings,this.eventBus); //on crée le monde
        eventBus.postQueued(new WorldCreatedEvent(this.world));//on lance l'event pour gamePanel

        this.gameLoop = new GameLoop(this.world); 
        new Thread(this.gameLoop).start(); // On lance le thread
        this.monitoring = false; //on coupe le thread de lancement
    }

    private void monitorBusForStart() {//un bus en attendant celui du jeu
        while (monitoring) {
            this.eventBus.processQueued(); // Traite StartGameEvent
        }
    }
    }
