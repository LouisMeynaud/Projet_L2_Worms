package com.lllmtb.ui.graphical;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;



public class Utilitaire {


    public static <T> JButton createImageButton(
        Map<T, BufferedImage> textureMap, T name, int buttonWidth, int buttonHeight, int iconWidth, int iconHeight, String text, String font, int fontSize, Color fontColor) {
        JButton button = new JButton();

        try {
            BufferedImage imgButton = textureMap.get(name);
            BufferedImage imgButtonHover = textureMap.get(name + "Hover");
            if (imgButton == null) {
                throw new Exception("Image introuvable dans la map");
            }

            Image imgButtonHoverScale = imgButtonHover.getScaledInstance(iconWidth, iconHeight, Image.SCALE_REPLICATE);
            Image imgButtonScale = imgButton.getScaledInstance(iconWidth, iconHeight, Image.SCALE_REPLICATE);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setIcon(new ImageIcon(imgButtonScale));
            button.setRolloverIcon(new ImageIcon(imgButtonHoverScale));

        } catch (Exception e) {
            Logger.logger.error("Error image " + name + " : " + e.getMessage());
            button.setText(name.toString());
        }
        button.setFocusable(false);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        button.setText(text);
        button.setFont(new Font(font, Font.BOLD, fontSize));
        button.setForeground(fontColor);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
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



    public static JButton createWeaponButton(WeaponId weaponkey, int ammoCount, boolean isUnlocked) {
        String searchKey;
        //l'image a chercher (lock ou non)
        if (isUnlocked) {
            searchKey = weaponkey.toString();
        } else {
            searchKey =  "Locked" + weaponkey.toString();

        }
        String ammoKey = "Ammo" + weaponkey.toString();
        return createBaseItemButton(searchKey, ammoKey, ammoCount, isUnlocked, TextureManager.WEAPONS);
    }


    public static JButton createToolButton(ToolId id, int ammoCount, boolean isUnlocked) {
        String searchKey = id.toString();
        String ammoKey = "Ammo" + id.toString();


        return createBaseItemButton(searchKey, ammoKey, ammoCount, isUnlocked, TextureManager.TOOLS);
    }


    private static JButton createBaseItemButton(String searchKey, String ammoKey, int ammoCount, boolean isUnlocked, Map<String, BufferedImage> texture) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(160, 160));
        button.setBackground(Color.DARK_GRAY);

        JLabel caseLabel = new JLabel("", SwingConstants.CENTER); // Label vide par défaut // place se qu'il contient au milieu
        try {
            BufferedImage img = texture.get(searchKey);

            if (img != null) {
                Image scaled = img.getScaledInstance(96, 96, Image.SCALE_REPLICATE); //replicate: resize sans floutage des pixel
                caseLabel.setIcon(new ImageIcon(scaled));
            }
        } catch (Exception e) {
            Logger.logger.error("Error image " + searchKey + " : " + e.getMessage());
             }

        button.add(caseLabel, BorderLayout.CENTER);

        if (isUnlocked) {
            button.setBackground(Color.LIGHT_GRAY);
            JPanel ammoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            ammoPanel.setOpaque(false);

            try {
                BufferedImage ammoImg = texture.get(ammoKey);
                if (ammoImg != null) {
                    Image scaledAmmo = ammoImg.getScaledInstance(32, 32, Image.SCALE_REPLICATE);
                    ammoPanel.add(new JLabel(new ImageIcon(scaledAmmo)));
                }
            } catch (Exception e) {
                Logger.logger.error("Error image " + ammoKey + " : " + e.getMessage());
            }

            //nombre
            JLabel nbLabel = new JLabel(String.valueOf(ammoCount));
            nbLabel.setFont(new Font("Arial", Font.BOLD, 12));
            ammoPanel.add(nbLabel);

            button.add(ammoPanel, BorderLayout.SOUTH);
        }

        return button;
    }

    public static int getHashId(int x, int y, int max) {
    int h = x * 374761393 + y * 668265263;
    h = (h ^ (h >> 13)) * 1274126177;
    h = h ^ (h >> 16);
    return (Math.abs(h) % max);
}

}
