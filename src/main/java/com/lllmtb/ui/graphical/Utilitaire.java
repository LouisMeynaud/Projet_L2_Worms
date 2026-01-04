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


/**
 * classe utilitaire fournissans des methode statique pour eviter de dupliquer du code.
 * Contient par exemple des methode pour crée des bouton avec des image , des slider , generer un "hashCode" etc.
 */
public class Utilitaire {


    /**
     * cree un bouton swing personnaliser avec une image de fond et une image de survol(HOVER).
     * @param <T> Le type de la cle dans la map contenant les texture
     * @param textureMap Le map contenant les texture
     * @param name la cle de l'image dans le map
     * @param buttonWidth la largeur du bouton
     * @param buttonHeight la hauteur du bouton
     * @param iconWidth la largeur de l'image
     * @param iconHeight la hauteur de l'image
     * @param text le texte a afficher
     * @param font le nom de la police d'ecriture
     * @param fontSize la taille de la police
     * @param fontColor la couleur du texte
     * @return le bouton personnalisé
     */
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
            Logger.logger.error("Error loading image {}", name, e);
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
    /**
     * crée un slider avec des parametre par defaut (evite la duplication de code).
     * @param min le minimun souhaiter
     * @param max le maximun souhaiter
     * @param value la valeur par defaut
     * @param spacing l'espace entre deux valeur
     * @return le slider
     */
    public static JSlider createSlider(int min, int max, int value, int spacing) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, value);

        slider.setMajorTickSpacing(spacing);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        return slider;
    }

    /**
     * crée les bouton d'inventaire pour une arme.
     * gere l'affichage de l'icone locked si une arme est pas debloquer
     * @param weaponKey l'id de l'arme
     * @param ammoCount le nombre de munition
     * @param isUnlocked le boolean indiquant si l'arme est unlock ou non
     * @param size la taille du bouton
     * @return le bouton
     */
    public static JButton createWeaponButton(WeaponId weaponKey, int ammoCount, boolean isUnlocked, int size) {
        String searchKey = isUnlocked ? weaponKey.toString() : ("Locked" + weaponKey.toString());
        String ammoKey = "Ammo" + weaponKey;
        return createBaseItemButton(searchKey, ammoKey, ammoCount, isUnlocked, TextureManager.WEAPONS, size);
    }

   /**
    * crée un bouton d'inventaire pour un outill.
    * @param id l'id de l'outil
    * @param ammoCount le nombre de munition
    * @param isUnlocked le boolean indiquant si l'outils est unlock ou non
    * @param size la taille du bouton
    * @return le bouton
    */
    public static JButton createToolButton(ToolId id, int ammoCount, boolean isUnlocked, int size) {
        String searchKey = id.toString();
        String ammoKey = "Ammo" + id;

        return createBaseItemButton(searchKey, ammoKey, ammoCount, isUnlocked, TextureManager.TOOLS, size);
    }


    /**
     * methode pour crée les bouton de la grille d'inventaire
     * le bouton a au centre l'image de l'arme/outils,et en dessous l'image des munition ainsi que le nombre restant.
     * @param searchKey La cle de l'image principale
     * @param ammoKey la clé de l'image de munique
     * @param ammoCount le nombre de munition
     * @param isUnlocked le boolean indiquant si l'objet est unlock ou non
     * @param texture le texture map ou chercher les image
     * @param size la taille du bouton
     * @return le bouton
     */
    private static JButton createBaseItemButton(String searchKey, String ammoKey, int ammoCount, boolean isUnlocked, Map<String, BufferedImage> texture, int size) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(size, size));
        button.setBackground(Color.DARK_GRAY);

        JLabel caseLabel = new JLabel("", SwingConstants.CENTER); // Label vide par défaut // place se qu'il contient au milieu
        try {
            BufferedImage img = texture.get(searchKey);

            if (img != null) {
                int imgSize = Math.max(1, size - 25);
                Image scaled = img.getScaledInstance(imgSize, imgSize, Image.SCALE_REPLICATE); //replicate: resize sans floutage des pixel
                caseLabel.setIcon(new ImageIcon(scaled));
            }
        } catch (Exception e) {
            Logger.logger.error("Error loading image {}", searchKey, e);
        }

        button.add(caseLabel, BorderLayout.CENTER);

        if (isUnlocked) {
            button.setBackground(Color.LIGHT_GRAY);
            JPanel ammoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            ammoPanel.setOpaque(false);

            try {
                BufferedImage ammoImg = texture.get(ammoKey);
                if (ammoImg != null) {
                    int ammoSize = Math.max(12, size / 5);
                    Image scaledAmmo = ammoImg.getScaledInstance(ammoSize, ammoSize, Image.SCALE_REPLICATE);
                    ammoPanel.add(new JLabel(new ImageIcon(scaledAmmo)));
                }
            } catch (Exception e) {
                Logger.logger.error("Error loading image {}", ammoKey, e);
            }

            //nombre

            String ammoDisplay = ammoCount < 0 ? "∞" : String.valueOf(ammoCount);
            JLabel nbLabel = new JLabel(ammoDisplay);
            nbLabel.setFont(new Font("Arial", Font.BOLD, Math.max(10, size / 8)));
            ammoPanel.add(nbLabel);

            button.add(ammoPanel, BorderLayout.SOUTH);
        }

        return button;
    }

    /**
     * crée un "hascode" compris entre 0 et max a partir de deux nombre(la position x y d'un bloc par exemple pour avoir une texture).
     * @param x un int
     * @param y un int
     * @param max le nombre max voulu
     * @return un entier entre 0 et max-1
     */
    public static int getHashId(int x, int y, int max) {
    int h = x * 374761393 + y * 668265263;
    h = (h ^ (h >> 13)) * 1274126177;
    h = h ^ (h >> 16);
    return (Math.abs(h) % max);
}
    /**
     * decoupe une image (un spriteSheet) en un tableau d'image pour les animations.
     * permet de charger des animation ou toute les frame sont sur une meme image
     * @param sheet l'image a decouper
     * @param cols le nombre de colonne sur l'image
     * @param rows le nombre de ligne sur l'image
     * @return le tableau contenant les differente frames.
     */
    public static BufferedImage[] cutSpriteSheet(BufferedImage sheet, int cols, int rows) {
        if (sheet == null) {
            return new BufferedImage[0];
        }

        int frameWidth = sheet.getWidth() / cols;
        int frameHeight = sheet.getHeight() / rows;
        BufferedImage[] sprites = new BufferedImage[cols * rows];

        int count = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                sprites[count] = sheet.getSubimage(
                        x * frameWidth,
                        y * frameHeight,
                        frameWidth,
                        frameHeight
                );
                count++;
            }
        }
        return sprites;
    }
    /**
     * crée un bouton pour le shop avec une image et un texte.
     * change la couleur du texte en fonction de si l'objet peut etre acheter ou non.
     * @param img l'image de l'objet
     * @param text le texte a afficher
     * @param canBuy boolean indiquant si l'objet peut etre acheter ou non
     * @return le bouton
     */
    public static JButton createShopButton(BufferedImage img, String text, boolean canBuy) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setPreferredSize(new Dimension(80, 80));
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        if (img != null) {
            Image scaled = img.getScaledInstance(48, 48, Image.SCALE_REPLICATE);
            button.setIcon(new ImageIcon(scaled));
        }

        button.setText(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        if (canBuy) {
            button.setForeground(Color.BLUE);
            button.setBackground(Color.LIGHT_GRAY);
        } else {
            button.setForeground(Color.RED);
            button.setBackground(Color.DARK_GRAY);
        }
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        return button;
    }
}