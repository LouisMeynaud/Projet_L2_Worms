package com.lllmtb.ui.graphical.SettingsPanel;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.ui.graphical.ScreenHomeEvent;
import com.lllmtb.events.ui.graphical.ScreenLoadGame;
import com.lllmtb.events.ui.graphical.ScreenSettingsGame;
import com.lllmtb.ui.graphical.Utilitaire.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire.Utilitaire;

public class NewOrLoadPanel extends JPanel {

    final EventBus systemBus;

    /**
     * Constructor of a panel that allows to choose between creating a new game or loading an existing one.
     * @param systemBus the system bus of the game
     */
    public NewOrLoadPanel(EventBus systemBus) {
        this.systemBus = systemBus;
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 0, 15, 0);


        JButton newGameButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 280, 140, 280, 140, "Nouvelle partie", "Arial", 23, Color.WHITE);
        JButton loadGameButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 280, 140, 280, 140, "Charger une partie", "Arial", 23, Color.WHITE);
        JButton backButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 280, 140, 280, 140, "Retour", "Arial", 23, Color.WHITE);

        newGameButton.addActionListener(e -> systemBus.postQueued(new ScreenSettingsGame()));
        loadGameButton.addActionListener(e -> systemBus.postQueued(new ScreenLoadGame()));
        backButton.addActionListener(e -> systemBus.postQueued(new ScreenHomeEvent()));

        gbc.gridy = 0;
        this.add(newGameButton, gbc);
        gbc.gridy = 1;
        this.add(loadGameButton, gbc);
        gbc.gridy = 2;
        this.add(backButton, gbc);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage backgroundImage = TextureManager.BACKGROUND.get("Background2");
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }


}
