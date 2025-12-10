package com.lllmtb.ui.graphical;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.ui.graphical.ScreenNewOrLoadEvent;

public class HomeScreenPanel extends JPanel {

    /**
     * The main page is used to open the settings, launch a game, or quit the game.
     * @param eventBus the eventBus of the game.
     */
    public HomeScreenPanel(EventBus eventBus) {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 0, 15, 0);


        JButton playButton = Utilitaire.createImageButton(TextureManager.UI, "Start", 128, 64, 128, 64,"Start","Arial",16);
        JButton settingButton = Utilitaire.createImageButton(TextureManager.UI, "GameSetting", 128, 64, 128, 64,"Game Settings","Arial",16);
        JButton closeButton = Utilitaire.createImageButton(TextureManager.UI, "Close", 128, 64, 128, 64,"Close","Arial",16);

        playButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenNewOrLoadEvent());
        });
        gbc.gridy = 0;
        this.add(playButton, gbc);
        gbc.gridy = 1;
        this.add(settingButton, gbc);
        gbc.gridy = 2;
        this.add(closeButton, gbc);
    }
}
