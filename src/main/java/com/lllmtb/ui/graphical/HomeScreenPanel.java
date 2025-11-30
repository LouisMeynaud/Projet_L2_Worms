package com.lllmtb.ui.graphical;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.ui.graphical.ScreenNewOrLoadEvent;

public class HomeScreenPanel extends JPanel{
    EventBus eventBus;
    
    
    /**
     * 
     * @param eventBus the eventBus of the game.
     * 
     * The main page is used to open the settings, launch a game, or quit the game.
     * 
     * 
     */
    public HomeScreenPanel(EventBus eventBus){
        this.eventBus = eventBus;
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.insets = new Insets(15, 0, 15, 0);
        
        
        JButton playButton = new JButton("Play");
        JButton settingsButton = new JButton("Game Settings");
        JButton exitButton = new JButton("Exit");
        


        playButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenNewOrLoadEvent());
        });
        gbc.gridy = 0;
        this.add(playButton,gbc);
        gbc.gridy = 1;
        this.add(settingsButton,gbc);
        gbc.gridy = 2;
        this.add(exitButton,gbc);
    }
}
