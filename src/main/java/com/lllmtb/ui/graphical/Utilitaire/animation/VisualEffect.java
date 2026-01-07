package com.lllmtb.ui.graphical.Utilitaire.animation;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.lllmtb.physics.Position;

public class VisualEffect {
    private final Position position;
    private final Animation animation;
    private final double width;
    private final double height;

    /**
     * represente un effet visuel temporaire dans le jeux (exemple explosion)
     * (l'animation est configurer pour ce jouer que une fois)
     * @param position la position ou l'on vas dessiner l'effet
     * @param animation l'animation que l'on veux dessiner
     * @param width la largeur de l'animation
     * @param height la hauteur de l'animation
     */
    public VisualEffect(Position position, Animation animation, double width, double height) {
        this.position = position;
        this.animation = animation;
        this.animation.setPlayOnce(true);
        this.animation.start();
        this.width = width;
        this.height = height;
    }
    /**
     * met a jour l'etat de l'animation (passe a la frame suivante si non fini)
     */
    public void update() {
        animation.update();
    }
    /**
     * verifie si l'animation est fini
     * @return true si l'animation est fini
     */
    public boolean isFinished() {
        return animation.hasEnded();
    }

    /**
     * dessine l'effet sur l'ecran en applicant les transformation de coordonée necessaire.
     * @param g2d le pinceaux
     * @param tileSize la taille des tuile a l'ecran (pour adapter la taille du dessins)
     * @param mapHeightPixels la hauteur de la carte en pixel (pour l'invertion de l'axe Y)
     */
    public void draw(Graphics2D g2d, int tileSize, double mapHeightPixels) {
        BufferedImage sprite = animation.getSprite();

        double effectWidthPx = width * tileSize;
        double effectHeightPx = height * tileSize;

        double xEcran = position.getX() * tileSize;
        double yEcran = mapHeightPixels - (position.getY() * tileSize);

        double drawX = xEcran - (effectWidthPx / 2.0);
        double drawY = yEcran - (effectHeightPx / 2.0);

        AffineTransform tx = new AffineTransform();
        tx.translate(drawX, drawY);

        double scaleX = effectWidthPx / sprite.getWidth();
        double scaleY = effectHeightPx / sprite.getHeight();
        tx.scale(scaleX, scaleY);

        g2d.drawImage(sprite, tx, null);
    }
}