package com.lllmtb.ui.graphical;

import java.awt.image.BufferedImage;
import java.util.Map;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import com.lllmtb.model.item.weapon.WeaponId;



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

    public static JSlider createSlider(int min, int max, int value, int spacing) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);  
        
        slider.setMajorTickSpacing(spacing); 
        slider.setSnapToTicks(true); 
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        
        return slider;
    }



    public static JButton createWeaponButton(WeaponId weaponkey, String ammoKey, int ammoCount, boolean isUnlocked) {
        Map<String, BufferedImage> texture = TextureManager.WEAPONS;
        JButton button = new JButton();
        button.setLayout(new BorderLayout()); 
        button.setPreferredSize(new Dimension(160, 160));
        
        JLabel caseLabel = new JLabel("", SwingConstants.CENTER); // Label vide par défaut // place se qu'il contient au milieu
        String searchKey;
        //l'image a chercher (lock ou non)
        if(isUnlocked){
            searchKey = weaponkey.toString();
        }else{
            searchKey =  "Locked" + weaponkey.toString();

        }
        //on cherche
        try {
            BufferedImage img = texture.get(searchKey);
            
            if (img != null) {
                Image scaled = img.getScaledInstance(96, 96, Image.SCALE_REPLICATE);//replicate: resize sans floutage des pixel
                caseLabel.setIcon(new ImageIcon(scaled));
            }
        } catch (Exception e) { 
            System.err.println("erreur image " + searchKey + " : " + e.getMessage());
             }

        button.add(caseLabel, BorderLayout.CENTER);
        
        
        if (isUnlocked) {
            JPanel ammoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            ammoPanel.setOpaque(false); 

            try {
                BufferedImage ammoImg = texture.get(ammoKey);
                if (ammoImg != null) {
                    Image scaledAmmo = ammoImg.getScaledInstance(32, 32, Image.SCALE_REPLICATE);
                    ammoPanel.add(new JLabel(new ImageIcon(scaledAmmo)));
                }
            } catch (Exception e) { 
                System.err.println("erreur image " + ammoKey + " : " + e.getMessage());
            }

            //nombre
            JLabel nbLabel = new JLabel(String.valueOf(ammoCount));
            nbLabel.setFont(new Font("Arial", Font.BOLD, 12));
            ammoPanel.add(nbLabel);

            button.add(ammoPanel, BorderLayout.SOUTH);
        }

        return button;
    }

    

}
