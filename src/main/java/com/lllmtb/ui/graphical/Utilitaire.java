package com.lllmtb.ui.graphical;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Color;

import javax.swing.border.BevelBorder;



public class Utilitaire {
    


    public static <T> JButton createImageButton(Map<T, BufferedImage> textureMap, T name, int buttonWidth, int buttonHeight, int iconWidth, int iconHeight) {
        JButton button = new JButton();
        
        try {
            BufferedImage imgRaw = textureMap.get(name);
            if (imgRaw == null) {
                throw new Exception("Image introuvable dans la map");
            }

            Image scaledImage = imgRaw.getScaledInstance(iconWidth, iconHeight, Image.SCALE_REPLICATE);
            button.setIcon(new ImageIcon(scaledImage));

        } catch (Exception e) {
            System.err.println("erreur image " + name + " : " + e.getMessage());
            button.setText(name.toString());
        }
        button.setFocusable(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));

        return button;
    }

    

}
