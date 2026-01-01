package com.lllmtb.ui.graphical;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.ui.graphical.ScreenHomeEvent;
import com.lllmtb.model.entity.team.Team;

public class EndGameScreenPanel extends JPanel {
    JLabel winnerLabel;

    /**
     * This page is used for the endgame.
     * @param eventBus the eventBus of the game.
     */
    public EndGameScreenPanel(EventBus eventBus) {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 0, 15, 0);

        this.winnerLabel = new JLabel("");
        JButton replayButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 256, 128, 256, 128, "Replay", "Arial", 23, Color.WHITE);
        JButton closeButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 256, 128, 256, 128, "Close", "Arial", 23, Color.WHITE);

        replayButton.addActionListener(e -> {
            eventBus.postQueued(new ScreenHomeEvent());
        });

        closeButton.addActionListener(e -> {
            System.exit(0);
        });
        gbc.gridy = 0;
        this.add(winnerLabel, gbc);
        gbc.gridy = 1;
        this.add(replayButton, gbc);
        gbc.gridy = 2;
        this.add(closeButton, gbc);

    }

    public void setWinner(Team team, GameWorld world) {
        winnerLabel.setText("Winner : " + team.getName());
        winnerLabel.setForeground(Color.WHITE);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        world.stop();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage backgroundImage = TextureManager.BACKGROUND.get("Background");
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }

}
