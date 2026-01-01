package com.lllmtb.ui.graphical;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.ui.graphical.ScreenHomeEvent;

public class MusicSettingsScreenPanel extends JPanel {

    /**
     * The page is used to set the music settings or to back to the home screen game.
     * @param eventBus the eventBus of the game.
     */
    public MusicSettingsScreenPanel(EventBus eventBus) {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JButton backButton = Utilitaire.createImageButton(TextureManager.UI, "Panel", 256, 128, 256, 128, "Back", "Arial", 23, Color.WHITE);

        JLabel musiqueVolumeLabel = new JLabel("Volume de la musique");
        JLabel sonorEffectVolumeLabel = new JLabel("Volume des effets sonores");
        JSlider musiqueVolumeSlider = Utilitaire.createSlider(0, 100, SoundPlayer.getMusiqueVolume(), 50);
        JSlider soundEffectVolumeSlider = Utilitaire.createSlider(0, 100, SoundPlayer.getSoundEffectVolume(), 50);

        JButton musique1 = Utilitaire.createImageButton(TextureManager.UI, "Panel", 256, 128, 256, 128, "Musique 1", "Arial", 23, Color.WHITE);
        JButton musique2 = Utilitaire.createImageButton(TextureManager.UI, "Panel", 256, 128, 256, 128, "Musique 2", "Arial", 23, Color.WHITE);
        JButton musique3 = Utilitaire.createImageButton(TextureManager.UI, "Panel", 256, 128, 256, 128, "Musique 3", "Arial", 23, Color.WHITE);


        musiqueVolumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        musiqueVolumeLabel.setForeground(Color.WHITE);

        sonorEffectVolumeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sonorEffectVolumeLabel.setForeground(Color.WHITE);

        musiqueVolumeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        musiqueVolumeSlider.setSnapToTicks(false);
        musiqueVolumeSlider.setPaintTicks(false);
        musiqueVolumeSlider.setForeground(Color.WHITE);

        soundEffectVolumeSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        soundEffectVolumeSlider.setSnapToTicks(false);
        soundEffectVolumeSlider.setPaintTicks(false);
        soundEffectVolumeSlider.setForeground(Color.WHITE);


        backButton.addActionListener(e -> eventBus.postQueued(new ScreenHomeEvent()));

        musiqueVolumeSlider.addChangeListener(e ->  SoundPlayer.setMusiqueVolume(musiqueVolumeSlider.getValue()));
        soundEffectVolumeSlider.addChangeListener(e -> SoundPlayer.setSoundEffectVolume(soundEffectVolumeSlider.getValue()));

        musique1.addActionListener(e -> SoundPlayer.playMusique("MusiqueGame1"));
        musique2.addActionListener(e -> SoundPlayer.playMusique("MusiqueGame2"));
        musique3.addActionListener(e -> SoundPlayer.playMusique("MusiqueGame3"));


        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 5, 0);

        gbc.gridy = 0;
        this.add(musiqueVolumeLabel, gbc);

        gbc.gridy = 1;
        this.add(musiqueVolumeSlider, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 5, 0);
        this.add(sonorEffectVolumeLabel, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 5, 0);
        this.add(soundEffectVolumeSlider, gbc);

        JPanel musiqueChoice = new JPanel();
        musiqueChoice.setOpaque(false);

        musiqueChoice.add(musique1);
        musiqueChoice.add(musique2);
        musiqueChoice.add(musique3);

        gbc.gridy = 4;
        gbc.insets = new Insets(20, 0, 5, 0);
        this.add(musiqueChoice, gbc);

        gbc.gridy = 5;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.insets = new Insets(0, 0, 20, 20);
        this.add(backButton, gbc);

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage backgroundImage = TextureManager.BACKGROUND.get("Background");
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }

}
