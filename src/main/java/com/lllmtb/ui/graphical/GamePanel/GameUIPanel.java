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

        JButton pauseButton = createButton("Pause");
        JButton inventoryButton = createButton("InventoryButton");
        JButton shootModeButton = createButton("ShootMode");
        pauseButton.addActionListener(e -> PauseMenu(true));
        shootModeButton.addActionListener(e -> this.eventBus.postQueued(new ShootMode()));
        topBar.add(pauseButton);
        lowerBar.add(inventoryButton);
        lowerBar.add(shootModeButton);

        
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


    private JButton createButton(String name){
        JButton button = new JButton();
        System.out.println(name);
        try {
            BufferedImage imgRaw = TextureManager.UI.get(name);
            Image scaledImage = imgRaw.getScaledInstance(48, 48, Image.SCALE_REPLICATE);
            button.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Erreur image" + name + ":"  + e.getMessage());
            button.setText(name);
        }
        button.setFocusable(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        button.setPreferredSize(new Dimension(64, 64));

        return button;
    }
    }
