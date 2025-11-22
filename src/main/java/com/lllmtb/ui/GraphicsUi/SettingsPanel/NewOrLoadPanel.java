package com.lllmtb.ui.GraphicsUi.SettingsPanel;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.TheEvents.ScreenHomeEvent;
import com.lllmtb.events.TheEvents.ScreenSettingsGame;

public class NewOrLoadPanel extends  JPanel {
    
    EventBus eventBus;

    public NewOrLoadPanel(EventBus eventBus){
        this.eventBus = new EventBus();


        JButton NewButton = new JButton("new game");
        NewButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenSettingsGame());
        });
        this.add(NewButton);

        JButton backButton = new JButton("retour");
    backButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenHomeEvent());
        });
        this.add(backButton);
    }


}
