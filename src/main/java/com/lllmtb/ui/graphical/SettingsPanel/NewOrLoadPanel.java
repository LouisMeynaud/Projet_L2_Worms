package com.lllmtb.ui.graphical.SettingsPanel;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.graphicsUi.ScreenHomeEvent;
import com.lllmtb.events.graphicsUi.ScreenSettingsGame;

public class NewOrLoadPanel extends  JPanel {
    
    EventBus eventBus;

    public NewOrLoadPanel(EventBus eventBus){
        this.eventBus = new EventBus();


        JButton newButton = new JButton("new game");
        newButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenSettingsGame());
        });
        this.add(newButton);

        JButton backButton = new JButton("retour");
    backButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenHomeEvent());
        });
        this.add(backButton);
    }


}
