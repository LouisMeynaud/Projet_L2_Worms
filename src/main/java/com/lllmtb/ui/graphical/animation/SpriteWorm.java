package com.lllmtb.ui.graphical.animation;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteWorm {

    private static BufferedImage spriteSheet;

    public static BufferedImage loadSprite() {

        BufferedImage sprite = null;

        try {
            sprite = ImageIO.read(SpriteWorm.class.getResource("/sprites/worm/WormRight.png"));
        } catch (IOException e) {
            System.err.println("Erreur: Impossible de charger le sprite: WormRight");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Erreur: Fichier sprite non trouvé: /sprites/worm/WormRight.png");
            e.printStackTrace();
        }

        return sprite;
    }

    public static BufferedImage getSprite(int xGrid, int yGrid) {
        if (spriteSheet == null) {
            spriteSheet = loadSprite();
        }
        return spriteSheet.getSubimage(xGrid * 32, yGrid * 32, 32, 32);
    }

}
