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
        
        
        JButton startButton = Utilitaire.createImageButton(TextureManager.UI, "Start", 128, 64, 64, 48);
        JButton SettingButton = Utilitaire.createImageButton(TextureManager.UI, "GameSetting", 128, 64, 64, 48);
        JButton CloseButton = Utilitaire.createImageButton(TextureManager.UI, "Close", 128, 64, 64, 48);
        


        startButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenNewOrLoadEvent());
        });
        gbc.gridy = 0;
        this.add(startButton,gbc);
        gbc.gridy = 1;
        this.add(SettingButton,gbc);
        gbc.gridy = 2;
        this.add(CloseButton,gbc);
    }
}
