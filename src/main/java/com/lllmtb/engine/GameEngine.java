package com.lllmtb.engine;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.TheEvents.StartGameEvent;



public class GameEngine {

    
    private final EventBus eventBus;
    private GameWorld world; 

    
    public GameEngine(EventBus bus) {
        this.eventBus = bus;
        subscribeToEvents(); 
    }

    private void subscribeToEvents() {
        eventBus.subscribe(StartGameEvent.class, event -> {
            handleGameStart(event);
        });
    }

    
    private void handleGameStart(StartGameEvent event) {
        GameSettings settings = event.getSettings(); //on recup les setting
        

        this.world = new GameWorld(settings); //on crée le monde
        

        GameLoop gameLoop = new GameLoop(this.world);//on lance la boucle qui calcule les changement du monde
        new Thread(gameLoop).start();
        
    }

}
