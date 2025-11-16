package com.lllmtb.ui.GraphicsUi;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.TheEvents.ShowStartSettingsEvent;

public class HomeScreenPanel extends JPanel{
    EventBus eventBus;
    
    public HomeScreenPanel(EventBus eventBus){
        this.eventBus = eventBus;

    JButton startButton = new JButton("start");
        JButton SettingButton = new JButton("game Setting");
        JButton CloseButton = new JButton("close");
        


        startButton.addActionListener(e -> {
            eventBus.postQueued(new ShowStartSettingsEvent());
        });
        this.add(startButton);
        this.add(SettingButton);
        this.add(CloseButton);
    }
}
