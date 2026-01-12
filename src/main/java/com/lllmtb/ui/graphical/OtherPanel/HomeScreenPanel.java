package com.lllmtb.ui.graphical.OtherPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.*;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.CloseGameEvent;
import com.lllmtb.events.ui.graphical.ScreenMusicSettingsEvent;
import com.lllmtb.events.ui.graphical.ScreenNewOrLoadEvent;
import com.lllmtb.ui.graphical.Utilitaire.SoundPlayer;
import com.lllmtb.ui.graphical.Utilitaire.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire.Utilitaire;

public class HomeScreenPanel extends JPanel {

    /**
     * The main page is used to open the settings, launch a game, or quit the game.
     * @param systemBus the systemBus of the game.
     */
    public HomeScreenPanel(EventBus systemBus) {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 0, 15, 0);

        JButton playButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 280, 140, 280, 140, "Jouer", "Arial", 23, Color.WHITE);
        JButton settingButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 280, 140, 280, 140, "Paramètres audio", "Arial", 23, Color.WHITE);
        JButton closeButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 280, 140, 280, 140, "Fermer", "Arial", 23, Color.WHITE);

        playButton.addActionListener(e -> systemBus.postQueued(new ScreenNewOrLoadEvent()));
        settingButton.addActionListener(e -> systemBus.postQueued(new ScreenMusicSettingsEvent()));
        closeButton.addActionListener(e -> systemBus.postQueued(new CloseGameEvent()));

        gbc.gridy = 0;
        this.add(playButton, gbc);
        gbc.gridy = 1;
        this.add(settingButton, gbc);
        gbc.gridy = 2;
        this.add(closeButton, gbc);

        if (SoundPlayer.getCurrentMusic() == null) SoundPlayer.playMusique("MusiqueGame1");


    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage backgroundImage = TextureManager.BACKGROUND.get("Background");
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }

}
