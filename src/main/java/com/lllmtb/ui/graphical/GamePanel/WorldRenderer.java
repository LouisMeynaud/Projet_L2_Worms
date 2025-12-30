package com.lllmtb.ui.graphical.GamePanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.model.entity.Coin;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.TeamColor;
import com.lllmtb.model.entity.worm.Statistics;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.GameMode;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.PhysicsModifier;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.map.Tile;
import com.lllmtb.ui.graphical.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire;
import com.lllmtb.ui.graphical.animation.Animation;
import com.lllmtb.ui.graphical.animation.AnimationManager;
import com.lllmtb.ui.graphical.animation.VisualEffect;

/**
 * Classe responsable du rendu graphique de la partit.
 *
 * elle gere le dessin de la carte (terrain), des entité(worms/projectile)
 * et des effet visuel(animation , barre de vie, current worms etc)
 */
public class WorldRenderer {
    private final GameWorldPanel panel;
    private final GameWorld world;
    private final List<VisualEffect> activeEffects = new ArrayList<>(); //le tableau stockant les effet en cours de dessinage.
    private final Map<TeamColor, Animation> teamAnimations = new HashMap<>(); //le tableau stockant les animation des worms
    private BufferedImage mapCache = null;

    /**
     * le constructeur.
     * @param panel le panel ou l'on dessine
     * @param world le monde du jeu
     */
    public WorldRenderer(GameWorldPanel panel, GameWorld world) {
        this.panel = panel;
        this.world = world;
    }

    /**
     * vide l'image de fond.
     *
     */
    public void clearMapCache() {
        this.mapCache = null;
    }

   /**
    *  ajoute un effet a dessinée dans activeEffects.
    *
    * @param animationKey la clé de l'animation chercher/a crée dans l'AnimationManager
    * @param radius la taille du dessin
    * @param pos la position de l'animation dans le monde physique
    */
    public void addEffect(String animationKey, double radius, com.lllmtb.physics.Position pos) {
        Animation anim = AnimationManager.create(animationKey, 7);
        if (anim != null) {
            double size = radius * 2.5;
            activeEffects.add(new VisualEffect(pos, anim, size, size));
        }
    }

    /**
     * Met a jour l'image de fond de la carte.
     * dessine les ciel et les tuiles de terrain dans un bufferedImage
     */
    protected void updateMapCache() {
        int width = panel.mapWidth();
        int height = panel.mapHeight();

        if (mapCache == null) {
            mapCache = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        int mapW = world.getMap().getWidth();
        int mapH = world.getMap().getHeight();

        Position center = new Position(mapW / 2.0, mapH / 2.0);
        int maxRadius = Math.max(mapW, mapH) + 10;

        updateMapCacheRegion(center, maxRadius);

        if (!panel.initialFitDone && panel.getWidth() > 0) {
            panel.fitMapToScreen();
        }
        panel.checkCameraBounds();
    }


    public void updateMapCacheRegion(Position center, int radius) {
        if (mapCache == null) {
            updateMapCache();
            return;
        }

        Graphics2D g2d = mapCache.createGraphics();

        int startX = Math.max(0, (int) center.getX() - radius - 2);
        int startY = Math.max(0, (int) center.getY() - radius - 2);
        int endX = Math.min(world.getMap().getWidth(), (int) center.getX() + radius + 2);
        int endY = Math.min(world.getMap().getHeight(), (int) center.getY() + radius + 2);

        Color skyColor;
        if (world.getGameMode() == GameMode.MOON) {
            skyColor = new Color(25, 10, 40); // Violet sombre
        } else {
            skyColor = new Color(135, 206, 235); // Bleu clair
        }
        g2d.setColor(skyColor);

        int pxX = startX * panel.tileSize;
        int pxY = startY * panel.tileSize;
        int pxWidth = (endX - startX) * panel.tileSize;
        int pxHeight = (endY - startY) * panel.tileSize;
        g2d.fillRect(pxX, pxY, pxWidth, pxHeight);

        Tile[][] mapMatrix = world.getMap().getMapMatrix();

        for (int y = startY; y < endY; y++) {
            if (y >= mapMatrix.length) break;
            for (int x = startX; x < endX; x++) {
                if (x >= mapMatrix[y].length) break;
                String tileType = mapMatrix[y][x].toString();
                if (mapMatrix[y][x] != Tile.AIR) {
                    BufferedImage texture;
                    tileType = tileType + Utilitaire.getHashId(x, y, mapMatrix[y][x].getNbTexture()); //trouve quel texture du bloc appliquer
                    texture = TextureManager.TILES.get(tileType);

                    if (texture != null) {
                        int screenX = x * panel.tileSize;
                        int screenY = y * panel.tileSize;
                        g2d.drawImage(texture, screenX, screenY, panel.tileSize, panel.tileSize, null);
                    }
                }
            }
        }
        g2d.dispose();
    }

    //////////////Animation and effect/////////////////////////////

    /**
     * recupere l'animation correspondante a la couleurs d'un vers.
     *
     * @param worm le worms duquel on veux l'animation
     * @return l'animation du worms
     */
    private Animation getWormAnimation(Worm worm) {
        TeamColor color = worm.getTeam().getTeamColor();
        if (teamAnimations.containsKey(color)) {
            return teamAnimations.get(color);
        }
        String key = "Worm" + color.toString();
        Animation anim = AnimationManager.create(key, 12);

        if (anim != null) {
            anim.start();
            teamAnimations.put(color, anim);
        }
        return anim;
    }

    /**
     * Update all worm animations.
     */
    protected void updateAnimations() {
        if (this.world == null || this.world.getPhysics() == null || this.world.getPhysics().getBodies() == null) {
            return;
        }
        for (PhysicsBody body : this.world.getPhysics().getBodies()) {
            Object entity = body.getEntity();
            if (entity instanceof Worm) {
                Worm worm = (Worm) entity;
                getWormAnimation(worm);
                worm.updateFacingDirection();
            }
        }
        for (Animation anim : teamAnimations.values()) {
            anim.update();
        }
    }

    /**
     * Iterating through the list of effects to draw the effects.
     * update them (move to the next frame and remove them from the list if they are finished)
     */
    protected void updateEffects() {
        for (int i = activeEffects.size() - 1; i >= 0; i--) {
            VisualEffect effect = activeEffects.get(i);
            effect.update();
            if (effect.isFinished()) {
                activeEffects.remove(i);
            }
        }
    }

    //////////////drawing/////////////////////////////

    /**
     * dessine tout les effets present dans activeEffects.
     * @param g2d le pinceaux
     */
    private void drawEffects(Graphics2D g2d) {
        double hauteurMapPixels = world.getMap().getHeight() * panel.tileSize;
        for (VisualEffect effect : this.activeEffects) {
            effect.draw(g2d, panel.tileSize, hauteurMapPixels);
        }
    }

    /**
     * dessine un worms.
     * @param g2d le pinceaux
     * @param worm le worms que l'on souhaite dessiner
     * @param activeWorm le worms que qui est entrain de jouer (pour dessiner les arme/la fleche sur lui)
     */
    private void drawWorm(Graphics2D g2d, PhysicsBody worm, Worm activeWorm) {
        double[] bounds = panel.getScreenBounds(worm); //calcule la position souhaite a l'ecran
        double xEcran = bounds[0];
        double yEcran = bounds[1];
        double largeurVerPx = bounds[2];
        double hauteurVerPx = bounds[3];
        Worm wormEntity = (Worm) worm.getEntity();
        BufferedImage textureWorm = getWormAnimation(wormEntity).getSprite(); //get la texture

        AffineTransform tx = new AffineTransform();
        tx.translate(xEcran - 2, yEcran - 12); // On place le crayon

        // Scale l'image du worms pour qu'elle soit a la bonne taille
        double echelleX = 1.875 * largeurVerPx / textureWorm.getWidth();
        double echelleY = 1.5 * hauteurVerPx / textureWorm.getHeight();
        tx.scale(echelleX, echelleY);

        // Flip l'image si le ver fait face a gauche
        int facingDir = wormEntity.getFacingDirection();
        if (facingDir == -1) {
            double spriteWidthScaled = textureWorm.getWidth() * echelleX;
            tx.translate(spriteWidthScaled / 2, 0);
            tx.scale(-1, 1);
            tx.translate(-spriteWidthScaled / 2, 0);
        }

        double xCenter = xEcran + (largeurVerPx / 2.0);
        double yCenter = yEcran + (hauteurVerPx / 2.0);
        paintEquipment(g2d, worm, xCenter, yCenter);

        paintHealthBar(g2d, (Worm) worm.getEntity(), xEcran, yEcran, largeurVerPx);

        g2d.drawImage(textureWorm, tx, null);

        if (worm.getEntity() == activeWorm) {
            paintCurrentWormsArrow(g2d, xEcran, yEcran, largeurVerPx, wormEntity.getTeam().teamColor);
            paintItem(g2d, xCenter, yCenter);
            paintAim(g2d, xCenter, yCenter);
        }
    }

    /**
     * dessine un projectile.
     *
     * @param g2d le pinceaux
     * @param projectile le projectile que l'on souhaite dessiner
     */
    private void drawProjectile(Graphics2D g2d, PhysicsBody projectile) {

        double[] bounds = panel.getScreenBounds(projectile); //calcule la position souhaite a l'ecran
        double xEcran = bounds[0];
        double yEcran = bounds[1];
        double largeurProjPx = bounds[2];
        double hauteurProjPx = bounds[3];

        BufferedImage textureProjectile = TextureManager.PROJECTILES.get(projectile.getEntity().getClass().getSimpleName());
        if (textureProjectile != null) {
            AffineTransform tx = new AffineTransform();

            tx.translate(xEcran, yEcran);
            double echelleX = largeurProjPx / textureProjectile.getWidth();
            double echelleY = hauteurProjPx / textureProjectile.getHeight();
            tx.scale(echelleX, echelleY);

            g2d.drawImage(textureProjectile, tx, null);
        }
    }

    private void drawCoin(Graphics2D g2d, PhysicsBody body) {
        double[] bounds = panel.getScreenBounds(body); // Convertit position monde -> écran
        double xEcran = bounds[0];
        double yEcran = bounds[1];
        double largeurPx = bounds[2];
        double hauteurPx = bounds[3];


        BufferedImage texture = TextureManager.UI.get("moneyIcon");
        AffineTransform tx = new AffineTransform();
        tx.translate(xEcran, yEcran);

        double echelleX = largeurPx / texture.getWidth();
        double echelleY = hauteurPx / texture.getHeight();
        tx.scale(echelleX, echelleY);

        g2d.drawImage(texture, tx, null);
    }

    /**
     * parcours la liste des physique body delegue le dessins de chaque entité au methode correspondanteZ.
     *
     * @param g2d pinceaux
     */
    private void paintEntity(Graphics2D g2d) {
        Worm activeWorm = world.getTurnManager().getTurn().getWormPlayable();

        if (world.getPhysics() != null && world.getPhysics().getBodies() != null) {
            for (PhysicsBody body : world.getPhysics().getBodies()) {
                if (body.getEntity() instanceof Worm) {
                    drawWorm(g2d, body, activeWorm);
                } else if (body.getEntity() instanceof Projectile) {
                    drawProjectile(g2d, body);
                } else if (body.getEntity() instanceof Coin) {
                    drawCoin(g2d, body);
                }
            }
        }
    }

    /**
     * dessine un fleche au dessus du worms qui joue.
     *
     * @param g2d le pinceaux
     * @param x la position x du worms a l'ecran
     * @param y la position y du worms a l'ecran
     * @param width la largeur du worms a l'ecran
     * @param color la couleur de la team
     */
    private void paintCurrentWormsArrow(Graphics2D g2d, double x, double y, double width, TeamColor color) {

        double offsetAnimation = Math.sin(world.getGameLoop().getTicks() / 10) * 5; //fonction sinuosidale , (qui monte et descend)
        int centerX = (int) (x + (width / 2.0));
        int bottomY = (int) (y - 35 + offsetAnimation);
        int size = 12;

        int[] xPoints = {centerX - size, centerX + size, centerX };
        int[] yPoints = {bottomY - size, bottomY - size, bottomY };
        Color paintColor;
        switch (color) {
            case GREEN : paintColor = Color.GREEN; break;
            case YELLOW : paintColor = Color.YELLOW; break;
            case RED : paintColor = Color.RED; break;
            case BLUE : paintColor = Color.BLUE; break;
            default : paintColor = Color.BLACK; break;
        }
        g2d.setColor(paintColor);
        g2d.fillPolygon(xPoints, yPoints, 3); //dessine le triange remplie

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawPolygon(xPoints, yPoints, 3); //dessine les contour
    }


    /**
     *
     * @param g2d le pinceaux
     * @param worm le worms au dessus duquel on dessine la barre de vie
     * @param x la position x a l'ecran
     * @param y la position y a l'ecran
     * @param width la largeur du worms a l'ecran
     */
    private void paintHealthBar(Graphics2D g2d, Worm worm, double x, double y, double width) {

        int maxHp = Statistics.getMaxHp();
        int currentHp = worm.getStats().getHp();

        int barHeight = 4;
        int barWidth = (int) width + 10;


        int barX = (int) (x - 5);
        int barY = (int) (y - barHeight - 10);

        g2d.setColor(Color.RED);
        g2d.fillRect(barX, barY, barWidth, barHeight);

        double ratio = (double) currentHp / maxHp;
        int greenBarWidth = (int) (barWidth * ratio);

        g2d.setColor(Color.GREEN);
        g2d.fillRect(barX, barY, greenBarWidth, barHeight);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(barX, barY, barWidth, barHeight);
    }


    /**
     * peint la barre rouge entre le worm et la souris qui fait office de viseur.
     *
     * @param g2d le pinceaux
     * @param xCenter le centre du vers a l'ecran
     * @param yCenter le centre du vers a l'ecran
     */
    private void paintAim(Graphics2D g2d, double xCenter, double yCenter) {
        Worm currentWorm = world.getTurnManager().getTurn().getWormPlayable();
        if (currentWorm != null && panel.shootMode) {
            g2d.setColor(Color.RED);

            ItemId handItem = world.getPlayingTeam().getHand();
            if (handItem != null && handItem.usesCrosshair()) {
                int crosshairSize = 15;
                int mouseX = (int) panel.mouseWorldX;
                int mouseY = (int) panel.mouseWorldY;
                g2d.setStroke(new BasicStroke(3)); // epaisseur du trait
                g2d.drawLine(mouseX - crosshairSize, mouseY, mouseX + crosshairSize, mouseY);
                g2d.drawLine(mouseX, mouseY - crosshairSize, mouseX, mouseY + crosshairSize);
            } else {
                double vecX = panel.mouseWorldX - xCenter;
                double vecY = panel.mouseWorldY - yCenter;
                double distance = Math.sqrt(vecX * vecX + vecY * vecY);
                double maxLen = 100.0;
                if (distance > maxLen) {
                    double ratio = maxLen / distance;
                    vecX = vecX * ratio;
                    vecY = vecY * ratio;
                }
                g2d.setStroke(new BasicStroke(3)); // epaisseur du trait
                g2d.drawLine((int) xCenter, (int) yCenter, (int) (xCenter + vecX), (int) (yCenter + vecY));
            }
        }
    }


    /**
     *
     * @param g2d le pinceaux
     * @param xCenter le centre du vers a l'ecran
     * @param yCenter le centre du vers a l'ecran
     */
    private void paintItem(Graphics2D g2d, double xCenter, double yCenter) {
        Worm activeWorm = world.getTurnManager().getTurn().getWormPlayable();
        if (activeWorm == null) return;

        ItemId handItem = world.getTurnManager().getTurn().getTeam().getHand();
        if (handItem == null) return;

        String textureName = "Hand" + handItem;
        BufferedImage texture = handItem instanceof WeaponId ? TextureManager.WEAPONS.get(textureName) : TextureManager.TOOLS.get(textureName);
        if (texture == null) return;

        AffineTransform tx = new AffineTransform();
        tx.translate(xCenter, yCenter);

        double angle = Math.atan2(panel.mouseWorldY - yCenter, panel.mouseWorldX - xCenter); // permet a partir d'une distance y et x entre worm et point de trouver un angle.
        tx.rotate(angle);

        if (Math.abs(angle) > Math.PI / 2) { // pour retourner l'image quand on regarde de l'autre coter.
            tx.scale(1, -1);
        }

        tx.translate(-texture.getWidth() / 2.0, -texture.getHeight() / 2.0);
        g2d.drawImage(texture, tx, null);
    }

    /**
     * Draws the active equipment on the worm (parachute, jetpack...).
     *
     * @param g2d the graphics context
     * @param worm the physics body of the worm
     * @param xCenter the x-coordinate of the worm's center on screen
     * @param yCenter the y-coordinate of the worm's center on screen
     */
    private void paintEquipment(Graphics2D g2d, PhysicsBody worm, double xCenter, double yCenter) {
        List<PhysicsModifier> modifiers = world.getPhysics().getModifiers().get(worm);
        if (modifiers == null) return;

        Set<ToolId> activeTools = modifiers.stream()
                .filter(modifier -> modifier.source instanceof ToolId)
                .map(modifier -> (ToolId) modifier.source)
                .collect(Collectors.toSet());

        for (ToolId tool : activeTools) {
            String textureName = "Equipped" + tool;
            BufferedImage texture = TextureManager.TOOLS.get(textureName);
            if (texture == null) return;
            AffineTransform tx = new AffineTransform();
            tx.translate(-texture.getWidth() / 2.0, -texture.getHeight() / 2.0);

            switch (tool) {
                case PARACHUTE -> tx.translate(xCenter, yCenter - 20);
                default -> tx.translate(xCenter, yCenter);
            }
            g2d.drawImage(texture, tx, null);
        }
    }

    /**
     * appelle les methode pour re/crée l'image de fond si besoin puis dessine l'image de fond sur notre image a l'ecran.
     * @param g2d le pinceaux
     */
    private void paintMap(Graphics2D  g2d) {
        if (!panel.initialFitDone && panel.getWidth() > 0) {
            panel.fitMapToScreen();
        }
        if (mapCache == null) {
            updateMapCache();
        }
        if (mapCache != null) {
            g2d.drawImage(mapCache, 0, 0, null);
        }
    }

    /**
     * la methode qui est appel par gameWorldPanel pour appel toute les methode necessaire au dessin.
     * @param g2d le pinceaux
     */
    public void render(Graphics2D g2d) {
        panel.checkCameraBounds();
        g2d.translate(panel.cameraOffsetX, panel.cameraOffsetY); //decalle le pinceau en fonction de la position camera (si negatif le dessin est deplacer vers la gauche)
        g2d.scale(panel.currentZoom, panel.currentZoom);

        paintMap(g2d);
        paintEntity(g2d);
        drawEffects(g2d);

    }
}