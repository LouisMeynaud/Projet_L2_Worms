package com.lllmtb.ui.graphical.SettingsPanel;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.ui.graphical.ScreenHomeEvent;
import com.lllmtb.events.ui.graphical.ScreenLoadGame;
import com.lllmtb.events.ui.graphical.ScreenSettingsGame;
import com.lllmtb.ui.graphical.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire;

public class NewOrLoadPanel extends JPanel {

    final EventBus eventBus;

    /**
     * Constructor of a panel that allows to choose between creating a new game or loading an existing one.
     * @param eventBus
     */
    public NewOrLoadPanel(EventBus eventBus) {
        this.eventBus = new EventBus();



        JButton newGameButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 150, 75, 150, 75, "New Game", "Arial", 16, Color.WHITE);
        newGameButton.addActionListener(e -> {

            eventBus.postQueued(new ScreenSettingsGame());
        });
        this.add(newGameButton);

        JButton loadGameButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 150, 75, 150, 75, "Load Game", "Arial", 16, Color.WHITE);
        loadGameButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenLoadGame());
        });
        this.add(loadGameButton);



        JButton backButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 150, 75, 150, 75, "Back", "Arial", 16, Color.WHITE);
    backButton.addActionListener(e -> {

            eventBus.postQueued(new ScreenHomeEvent());
        });
        this.add(backButton);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage backgroundImage = TextureManager.BACKGROUND.get("Background2");
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }


}
