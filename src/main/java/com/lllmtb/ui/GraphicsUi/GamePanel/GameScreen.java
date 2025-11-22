package com.lllmtb.ui.GraphicsUi.GamePanel;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import com.lllmtb.events.EventBus;

public class GameScreen extends JLayeredPane { 

    private GameWorldPanel gameWorldPanel;
    private GameUIPanel gameUIPanel;

    public GameScreen(EventBus bus) {
        gameWorldPanel = new GameWorldPanel(bus);
        gameUIPanel = new GameUIPanel(bus);
        this.add(gameWorldPanel, JLayeredPane.DEFAULT_LAYER);//default_layer : la couche du dessous
        this.add(gameUIPanel, JLayeredPane.PALETTE_LAYER); //pallette_layer = la couche du dessus
        Rectangle bounds = getBounds();
        gameWorldPanel.setBounds(bounds); // le jeu prend tout l'ecran
        gameUIPanel.setBounds(bounds);    

        this.addComponentListener(new ComponentAdapter() { //listenner qui se declenche quand on resize la taille du composant parent/fenetre
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();
                
                // On force les deux panneaux à prendre toute la place
                gameWorldPanel.setBounds(0, 0, width, height);
                gameUIPanel.setBounds(0, 0, width, height);
                revalidate();//permet de bien replacer les bouton etc
                repaint();
            }
        });

        
    }}

