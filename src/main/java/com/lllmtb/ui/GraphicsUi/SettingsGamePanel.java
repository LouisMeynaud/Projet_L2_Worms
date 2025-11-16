package com.lllmtb.ui.GraphicsUi;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.TheEvents.ShowHomeScreenEvent;
import com.lllmtb.events.TheEvents.StartGameEvent;

public class SettingsGamePanel extends JPanel{
    public EventBus eventBus;

    public SettingsGamePanel(EventBus eventBus){
        this.eventBus = eventBus;


    JButton backButton = new JButton("retour");
    backButton.addActionListener(e -> {
            eventBus.postQueued(new ShowHomeScreenEvent());
        });


    JButton starButton = new JButton("Start");
        starButton.addActionListener(e -> {
            // recupere les donnée
            GameSettings settings = setSettings();
            eventBus.postQueued(new StartGameEvent(settings));
        });

    this.add(starButton);
    this.add(backButton);

        
}public GameSettings setSettings(){

        return new GameSettings(1, new String[] {"test"}, 1, false, null, false);
    }
}
