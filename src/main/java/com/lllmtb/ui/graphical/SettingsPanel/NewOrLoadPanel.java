package com.lllmtb.ui.graphical.SettingsPanel;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.graphicsUi.ScreenHomeEvent;
import com.lllmtb.events.graphicsUi.ScreenSettingsGame;

public class NewOrLoadPanel extends  JPanel {
    
    EventBus eventBus;
    /**
     * 
     * @param eventBus the event bus of the game.
     * a simple panel allowing you to choose  to load or create a new game
     */
    public NewOrLoadPanel(EventBus eventBus){
        this.eventBus = new EventBus();


        JButton NewGameButton = new JButton("new game");
        NewGameButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenSettingsGame());
        });
        this.add(NewGameButton);

        JButton loadGameButton = new JButton("load game");
        loadGameButton.addActionListener(e -> {
        //
        });
        this.add(loadGameButton);


        JButton backButton = new JButton("retour");
    backButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenHomeEvent());
        });
        this.add(backButton);
    }


}
