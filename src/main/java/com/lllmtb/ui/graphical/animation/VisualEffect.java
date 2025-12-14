package com.lllmtb.ui.graphical.animation;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.lllmtb.physics.Position;

public class VisualEffect {
    private Position position;
    private Animation animation;
    private double width;
    private double height;

    public VisualEffect(Position position, Animation animation, double width, double height) {
        this.position = position;
        this.animation = animation;
        this.animation.setPlayOnce(true); 
        this.animation.start();
        this.width = width;
        this.height = height;
    }

    public void update() {
        animation.update();
    }

    public boolean isFinished() {
        return animation.hasEnded();
    }

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