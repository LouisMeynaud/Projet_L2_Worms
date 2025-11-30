package com.lllmtb.ui.graphical.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.GamePauseEvent;
import com.lllmtb.events.ui.ShootMode;
import com.lllmtb.ui.graphical.TextureManager; 

public class GameUIPanel extends JPanel {

    private EventBus eventBus;
    private JPanel pauseMenucontainer; 

    public GameUIPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        setOpaque(false); 
        setLayout(new BorderLayout());
        createPauseMenu();

       


        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBar.setOpaque(false);

        JPanel lowerBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lowerBar.setOpaque(false);


        
        
        this.add(topBar, BorderLayout.NORTH);
        this.add(lowerBar, BorderLayout.SOUTH);

        topBar.add(pauseButton());
        lowerBar.add(shootModeButton());
        
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
        
        this.revalidate(); 
        this.repaint();    
        
        this.eventBus.postQueued(new GamePauseEvent(show));
        if (!show) {
            SwingUtilities.getWindowAncestor(this).requestFocus();
        }
    }

    private JButton pauseButton(){
        JButton pauseButton = new JButton();
        try {
            BufferedImage imgRaw = TextureManager.UI.get("Pause");
            Image scaledImage = imgRaw.getScaledInstance(48, 48, Image.SCALE_REPLICATE);
            pauseButton.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Erreur image pause: " + e.getMessage());
            pauseButton.setText("pause");
        }
        pauseButton.setFocusable(false);
        pauseButton.setBackground(Color.LIGHT_GRAY);
        pauseButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pauseButton.setPreferredSize(new Dimension(64, 64));

        
        pauseButton.addActionListener(e -> PauseMenu(true));
        return pauseButton;
    }

    private JButton shootModeButton(){
        
        JButton shootModeButton = new JButton();
        try {
            BufferedImage imgRaw = TextureManager.UI.get("ShootMode");
            Image scaledImage = imgRaw.getScaledInstance(48, 48, Image.SCALE_REPLICATE);
            shootModeButton.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Erreur image shootMode: " + e.getMessage());
            shootModeButton.setText("shoot");
        }
        shootModeButton.setFocusable(false);
        shootModeButton.setBackground(Color.LIGHT_GRAY);
        shootModeButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        shootModeButton.setPreferredSize(new Dimension(64, 64));

        
        shootModeButton.addActionListener(e -> this.eventBus.postQueued(new ShootMode()));
        return shootModeButton;
    }
}