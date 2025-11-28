package com.lllmtb.ui.graphical.GamePanel;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import com.lllmtb.events.EventBus;

public class GameScreen extends JLayeredPane { 

    private GameWorldPanel gameWorldPanel;
    private GameUIPanel gameUIPanel;

   /**
    * This page will contain the overlay of the different page layers of the game.
    * @param bus 
    */
    public GameScreen(EventBus eventBus) {
        gameWorldPanel = new GameWorldPanel(eventBus);
        gameUIPanel = new GameUIPanel(eventBus);
        this.add(gameWorldPanel, JLayeredPane.DEFAULT_LAYER);//default_layer : la couche du dessous
        this.add(gameUIPanel, JLayeredPane.PALETTE_LAYER); //pallette_layer = la couche du dessus
        Rectangle bounds = getBounds();
        gameWorldPanel.setBounds(bounds); // the game take all the screen
        gameUIPanel.setBounds(bounds);    

        //Listener that triggers when the parent/window component is resized to resize the game
        this.addComponentListener(new ComponentAdapter() { 
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();
                
                // We force both panels to take up all the space
                gameWorldPanel.setBounds(0, 0, width, height);
                gameUIPanel.setBounds(0, 0, width, height);
                revalidate();//allows you to properly reposition the buttons etc
                repaint();
            }
        });

        
    }}

