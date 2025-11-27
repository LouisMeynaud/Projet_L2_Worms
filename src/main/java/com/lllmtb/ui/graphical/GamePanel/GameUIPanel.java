package com.lllmtb.ui.graphical.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.GamePauseEvent; 

public class GameUIPanel extends JPanel {

    private EventBus eventBus;
    private JPanel pauseMenucontainer; 

    public GameUIPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        setOpaque(false); 
        setLayout(new BorderLayout());

       
        JButton settingsButton = new JButton();
        try {
            BufferedImage imgRaw = ImageIO.read(getClass().getResource("/textures/button/Pause.png"));
            Image scaledImage = imgRaw.getScaledInstance(48, 48, Image.SCALE_REPLICATE);
            settingsButton.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Erreur image settings: " + e.getMessage());
            settingsButton.setText("Settings");
        }
        settingsButton.setBackground(Color.LIGHT_GRAY);
        settingsButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        settingsButton.setPreferredSize(new Dimension(64, 64));

        
        settingsButton.addActionListener(e -> PauseMenu(true));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setOpaque(false);
        topBar.add(settingsButton);
        
        this.add(topBar, BorderLayout.NORTH);

        
        createPauseMenu();
    }

   
    private void createPauseMenu() {
        this.pauseMenucontainer = new JPanel(new GridBagLayout()); // un conteneur pour pour centre le vrai pannel
        this.pauseMenucontainer.setOpaque(false);
        this.pauseMenucontainer.setVisible(false);

        
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS)); //Y_AXIS bouton les un sous les autre
        menuPanel.setBackground(Color.DARK_GRAY);
        menuPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); 

        
        JButton resumeButton = new JButton("Reprendre");
        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quitter");

       
        resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        resumeButton.addActionListener(e -> PauseMenu(false));

        
        menuPanel.add(resumeButton);
        menuPanel.add(Box.createVerticalStrut(10)); 
        menuPanel.add(saveButton);
        menuPanel.add(Box.createVerticalStrut(10)); 
        menuPanel.add(quitButton);

        
        this.pauseMenucontainer.add(menuPanel);
        
        this.add(this.pauseMenucontainer, BorderLayout.CENTER);
    }

   
    private void PauseMenu(boolean show) {
        this.pauseMenucontainer.setVisible(show);
        
        this.eventBus.postQueued(new GamePauseEvent(show));
        if (!show) {
            SwingUtilities.getWindowAncestor(this).requestFocus();
        }
    }
}