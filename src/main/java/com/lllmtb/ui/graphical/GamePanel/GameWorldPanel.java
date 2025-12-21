package com.lllmtb.ui.graphical.GamePanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.GamePauseEvent;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.events.game.MapUpdateEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.events.input.ProcessInputsEvent;
import com.lllmtb.events.move.MouseInputEvent;
import com.lllmtb.events.move.MouseWheelMoveInputEvent;
import com.lllmtb.events.physics.ExplosionEvent;
import com.lllmtb.events.ui.MoveEvent;
import com.lllmtb.events.ui.ShootMode;
import com.lllmtb.events.ui.graphical.RequestFocus;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.TeamColor;
import com.lllmtb.model.entity.worm.Statistics;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.GameMode;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.map.Tile;
import com.lllmtb.ui.graphical.SoundPlayer;
import com.lllmtb.ui.graphical.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire;
import com.lllmtb.ui.graphical.animation.Animation;
import com.lllmtb.ui.graphical.animation.AnimationManager;
import com.lllmtb.ui.graphical.animation.VisualEffect;

public class GameWorldPanel extends JPanel {
    private EventBus eventBus;  // system bus
    private GameWorld world;
    private Tile[][] mapMatrix;
    private final int tileSize = 12;
    private boolean shootMode;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private final List<VisualEffect> activeEffects = new ArrayList<>();

    private final Map<TeamColor, Animation> teamAnimations = new HashMap<>();

    private BufferedImage mapCache = null;

    private boolean isCameraMode = false;
    private boolean hasDraggedCamera = false;
    private boolean initialFitDone = false;
    private double currentZoom = 1.0;
    private double zoomSpeed = 0.2;

    private double cameraOffsetX = 0;
    private double cameraOffsetY = 0;
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private int currentMouseX = 0;
    private int currentMouseY = 0;
    private double mouseWorldX = 0;
    private double mouseWorldY = 0;



    public GameWorldPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        setFocusable(true);
        subscribeToSystemEvents();


        //////////imput////////////////

        addHierarchyListener(e -> {
            if (e.getChangeFlags() == HierarchyEvent.SHOWING_CHANGED && isShowing()) {
                requestFocusInWindow();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                if (pressedKeys.contains(KeyEvent.VK_UP)) {
                    world.getBus().postQueued(new MoveEvent(Direction.Up));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        });

        MouseAdapter mouseHandler = new MouseAdapter() {
            private long pressStartTime;

            @Override
            public void mousePressed(MouseEvent e) {
                pressStartTime = System.currentTimeMillis();
                lastMouseX = e.getX();
                lastMouseY = e.getY();

                if (e.getButton() == MouseEvent.BUTTON3) {
                    isCameraMode = true;
                    hasDraggedCamera = false;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int type = e.getButton();
                long duration = System.currentTimeMillis() - pressStartTime;

                if (type == MouseEvent.BUTTON1 && shootMode) {
                   Position worldPos = screenToWorld(e.getX(), e.getY());
                   ItemId item = world.getPlayingTeam().getHand();
                   if ((item != null && item instanceof WeaponId) && world.getTurnManager().getTurn().getTeam().getInventory().weapons.getAmmo(((WeaponId) item)) > 0) {
                   String sound = world.getPlayingTeam().getHand().toString();
                   SoundPlayer.playSound(sound + "Shot");
                }
                    world.getBus().postQueued(new MouseInputEvent(worldPos, duration, Direction.Left));
                }

                if (type == MouseEvent.BUTTON3) {
                    isCameraMode = false;
                    if (!hasDraggedCamera) {
                        int a = 0; //suprimer une fois l'implementation du else faite
                        //world.getBus().postQueued(new MouseInputEvent(new Position(worldX, worldY),duration, Direction.Right));

                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isCameraMode) {
                    currentMouseX = e.getX();
                    currentMouseY = e.getY();
                    mouseWorldX = (currentMouseX - cameraOffsetX) / currentZoom;
                    mouseWorldY = (currentMouseY - cameraOffsetY) / currentZoom;

                    double dx = currentMouseX - lastMouseX;
                    double dy = currentMouseY - lastMouseY;

                    hasDraggedCamera = true;
                    cameraOffsetX += dx;
                    cameraOffsetY += dy;
                    lastMouseX = currentMouseX;
                    lastMouseY = currentMouseY;

                    checkCameraBounds();
                    repaint();
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();

                if (isCameraMode) {
                    double centerX = getWidth() / 2.0;
                    double centerY = getHeight() / 2.0;

                    double mapPointX = (centerX - cameraOffsetX) / currentZoom;
                    double mapPointY = (centerY - cameraOffsetY) / currentZoom;

                    if (rotation < 0) {
                        currentZoom += zoomSpeed;
                    } else {
                        currentZoom -= zoomSpeed;
                    }

                    double minZoom = Math.max(getWidth() / mapWidth(), getHeight() / mapHeight());
                    if (currentZoom < minZoom) {
                        currentZoom = minZoom;
                    }
                    if (currentZoom > 8.0) {
                        currentZoom = 8.0;
                    }

                    cameraOffsetX = centerX - (mapPointX * currentZoom);
                    cameraOffsetY = centerY - (mapPointY * currentZoom);

                    checkCameraBounds();
                    repaint();
                } else {
                    if (rotation < 0) {
                        world.getBus().postQueued(new MouseWheelMoveInputEvent(Direction.Down));
                    }
                    if (rotation > 0) {
                        world.getBus().postQueued(new MouseWheelMoveInputEvent(Direction.Up));
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                currentMouseX = e.getX();
                currentMouseY = e.getY();
                mouseWorldX = (currentMouseX - cameraOffsetX) / currentZoom;
                mouseWorldY = (currentMouseY - cameraOffsetY) / currentZoom;
                repaint();

            }
        };

        addMouseListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }


    //////////////////utilitaire//////////////////:

   private Position screenToWorld(int screenX, int screenY) {
    double pixelX = (screenX - cameraOffsetX) / currentZoom;
    double pixelY = (screenY - cameraOffsetY) / currentZoom;

    double tileX = pixelX / tileSize;
    double tileYRaw = pixelY / tileSize;

    double mapHeightTiles = world.getMap().getHeight();
    double tileY = mapHeightTiles - tileYRaw;

    return new Position(tileX, tileY);
}
    private int mapWidth() {
        return world.getMap().getWidth() * tileSize;
    }

    private int mapHeight() {
        return world.getMap().getHeight() * tileSize;
    }

    /**
     * Process pressed keys and send movement events.
     * This method should be called every game tick for smooth continuous movement.
     */
    public void processInputs() {
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
            world.getBus().postQueued(new MoveEvent(Direction.Down));
        }
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            world.getBus().postQueued(new MoveEvent(Direction.Left));
        }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            world.getBus().postQueued(new MoveEvent(Direction.Right));
        }
    }

    public void subscribeToSystemEvents() {
        eventBus.subscribe(WorldCreatedEvent.class, event -> {
            this.world = event.getWorld();
            subscribeToWorldEvents();
            this.mapMatrix = this.world.getMap().getMapMatrix();
            this.mapCache = null; //on efface l'ancienne image
            this.initialFitDone = false;
            SoundPlayer.playMusique("MusiqueGame1");
            repaint();
        });

        eventBus.subscribe(GamePauseEvent.class, event -> {

        });
    }

    public void subscribeToWorldEvents() {
        if (this.world == null) return;

        world.getBus().subscribe(MapUpdateEvent.class, event -> {
            this.mapMatrix = this.world.getMap().getMapMatrix();
            this.mapCache = null;
            repaint();
        });
        world.getBus().subscribe(GameStateUpdatedEvent.class, event -> {
            updateAnimations();
            updateEffects();
            repaint();
        });

        world.getBus().subscribe(ShootMode.class, event -> {
            this.shootMode = !this.shootMode;
        });

        world.getBus().subscribe(RequestFocus.class, event -> {
            this.requestFocusInWindow();
        });

        world.getBus().subscribe(ProcessInputsEvent.class, event -> {
            processInputs();
        });

        world.getBus().subscribe(ExplosionEvent.class, event -> {
            Animation anim = AnimationManager.create(event.getAnimationKey(), 7);

            if (anim != null) {
                double size = event.getRadius() * 2.5;
                activeEffects.add(new VisualEffect(event.getPosition(), anim, size, size));
            }
            System.out.println(event.getAnimationKey());
            SoundPlayer.playSound(event.getAnimationKey());
        });

    }


    /////////////map/////////////////:

    private void fitMapToScreen() {

        double screenWidth = getWidth();
        double screenHeight = getHeight();
        double mapW = mapWidth();
        double mapH = mapHeight();


        this.currentZoom = screenWidth / mapW;

        double zoomedH = mapH * currentZoom;
        this.cameraOffsetX = 0;

        if (zoomedH > screenHeight) {
            this.cameraOffsetY = screenHeight - zoomedH;
        } else {
            this.cameraOffsetY = 0;
        }

        this.initialFitDone = true;
    }

    private void checkCameraBounds() {
        if (mapCache == null) {
            return;
        }

        double screenWidth = getWidth();
        double screenHeight = getHeight();

        double mapRealWidth = mapWidth();
        double mapRealHeight = mapHeight();

        double minZoom = Math.max(screenWidth / mapRealWidth, screenHeight / mapRealHeight);
        if (currentZoom < minZoom) {
            currentZoom = minZoom;
        }
        if (currentZoom > 8.0) {
            currentZoom = 8.0;
        }

        double zoomedWidth = mapRealWidth * currentZoom;
        double zoomedHeigth = mapRealHeight * currentZoom;

        double minX = screenWidth - zoomedWidth;
        if (cameraOffsetX > 0) {
            cameraOffsetX = 0;
        }
        if (cameraOffsetX < minX) {
            cameraOffsetX = minX;
        }

        double minY = screenHeight - zoomedHeigth;
        if (cameraOffsetY > 0) {
            cameraOffsetY = 0;
        }
        if (cameraOffsetY < minY) {
            cameraOffsetY = minY;
        }
    }
    private void updateMapCache() {
        int width = mapWidth();
        int height = mapHeight();

        mapCache = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mapCache.createGraphics(); //cree un crayon dans mapCache
        Color skyColor;
        if (world.getGameMode() == GameMode.MOON) {
            skyColor = new Color(25, 10, 40); // Violet sombre
        } else {
            skyColor = new Color(135, 206, 235); // Bleu clair
        }
        g2d.setColor(skyColor);
        g2d.fillRect(0, 0, width, height);

        for (int y = 0; y < mapMatrix.length; y++) {
            for (int x = 0; x < mapMatrix[y].length; x++) {
                String tileType = mapMatrix[y][x].toString();
                if (mapMatrix[y][x] != Tile.AIR) {
                BufferedImage texture;
                    tileType = tileType + Utilitaire.getHashId(x, y, mapMatrix[y][x].getNbTexture());
                    texture = TextureManager.TILES.get(tileType);

                if (texture != null) {
                    int screenX = x * tileSize;
                    int screenY = y * tileSize;
                    g2d.drawImage(texture, screenX, screenY, tileSize, tileSize, null);
                    }
                }
            }
        }
        g2d.dispose();

        if (!initialFitDone && getWidth() > 0) {
            fitMapToScreen();
        }
        checkCameraBounds();
    }


    //////////////dessins/////////////////////////////

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
    private void updateAnimations() {
        if (this.world == null || this.world.getPhysics() == null || this.world.getPhysics().getBodies() == null) {
            return;
        }
        for (PhysicsBody body : this.world.getPhysics().getBodies()) {
            Object entity = body.getEntity();
            if (entity instanceof Worm) {
                Worm worm = (Worm) entity;
                Animation anim = getWormAnimation(worm);
                if (anim != null) {
                    anim.update();
                }
                // Update facing direction based on velocity
                worm.updateFacingDirection();
            }
        }
    }

    /**
     * Iterating through the list of effects to draw the effects,
     * update them (move to the next frame and remove them from the list if they are finished)
     */
    private void updateEffects() {
        for (int i = activeEffects.size() - 1; i >= 0; i--) {
            VisualEffect effect = activeEffects.get(i);
            effect.update();
            if (effect.isFinished()) {
                activeEffects.remove(i);
            }
        }
    }

    private void drawEffects(Graphics2D g2d) {
        double hauteurMapPixels = world.getMap().getHeight() * this.tileSize;
        for (VisualEffect effect : this.activeEffects) {
            effect.draw(g2d, tileSize, hauteurMapPixels);
        }
    }

    private void paintEntity(Graphics2D g2d) {
        double hauteurMapPixels = world.getMap().getHeight() * this.tileSize;
        Worm activeWorm = world.getTurnManager().getTurn().getWormPlayable();

        if (world.getPhysics() != null && world.getPhysics().getBodies() != null) {
            for (PhysicsBody body : world.getPhysics().getBodies()) {
                 if (body.getEntity() instanceof Worm) {
                    PhysicsBody worm = body;

                    double largeurVerPx = worm.getWidth() * tileSize;
                    double hauteurVerPx = worm.getHeight() * tileSize;

                    double xEcran = worm.getPos().getX() * tileSize;
                    double yPhysique = worm.getPos().getY() * tileSize;
                    double yEcran = hauteurMapPixels - yPhysique - hauteurVerPx; // -hauteurVerPx car dessin commence en haut a gauche

                    Worm wormEntity = (Worm) worm.getEntity();
                    BufferedImage textureWorm = getWormAnimation(wormEntity).getSprite();

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

                     g2d.drawImage(textureWorm, tx, null);




                    paintHealthBar(g2d, (Worm) worm.getEntity(), xEcran, yEcran, largeurVerPx);


                        if (worm.getEntity() == activeWorm) {
                            double xCenter = xEcran + (largeurVerPx / 2.0);
                            double yCenter = yEcran + (hauteurVerPx / 2.0);
                            paintCurrentWorsArrow(g2d, xEcran, yEcran, largeurVerPx);
                            paintWeapon(g2d, xCenter, yCenter);
                            paintAim(g2d, xCenter, yCenter);
                        }

                 } else if (body.getEntity() instanceof Projectile) {
                    PhysicsBody projectile = body;
                    BufferedImage textureProjectile = TextureManager.PROJECTILES.get(projectile.getEntity().getClass().getSimpleName());
                    if (textureProjectile != null) {
                        double largeurProjPx = body.getWidth() * tileSize;
                        double hauteurProjPx = body.getHeight() * tileSize;

                        double xEcran = body.getPos().getX() * tileSize;
                        double yEcran = hauteurMapPixels - (body.getPos().getY() * tileSize) - hauteurProjPx;
                        AffineTransform tx = new AffineTransform();

                        tx.translate(xEcran, yEcran);
                        double echelleX = largeurProjPx / textureProjectile.getWidth();
                        double echelleY = hauteurProjPx / textureProjectile.getHeight();
                        tx.scale(echelleX, echelleY);

                        g2d.drawImage(textureProjectile, tx, null);
                    }
                }
            }
        }
    }

    private void paintCurrentWorsArrow(Graphics2D g2d, double x, double y, double width) {

        double offsetAnimation = Math.sin(world.getGameLoop().getTicks() / 10) * 5;
        int centerX = (int) (x + (width / 2.0));


        int bottomY = (int) (y - 35 + offsetAnimation);

        int size = 12;

        int[] xPoints = {centerX - size, centerX + size, centerX };
        int[] yPoints = {bottomY - size, bottomY - size, bottomY };

        g2d.setColor(Color.YELLOW);
        g2d.fillPolygon(xPoints, yPoints, 3);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawPolygon(xPoints, yPoints, 3);
    }



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


    private void paintAim(Graphics2D g2d, double xCenter, double yCenter) {
        Worm currentWorm = world.getTurnManager().getTurn().getWormPlayable();
        if (currentWorm != null && shootMode) {
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3)); // epaisseur du trait


        double vecX = mouseWorldX - xCenter;
        double vecY = mouseWorldY - yCenter;

        double distance = Math.sqrt(vecX * vecX + vecY * vecY);
        double maxLen = 100.0;

        if (distance > maxLen) {
            double ratio = maxLen / distance;
            vecX = vecX * ratio;
            vecY = vecY * ratio;
        }
        g2d.drawLine((int) xCenter, (int) yCenter, (int) (xCenter + vecX), (int) (yCenter + vecY));


        }
    }


    private void paintWeapon(Graphics2D g2d, double xCenter, double yCenter) {
        Worm activeWorm = world.getTurnManager().getTurn().getWormPlayable();
        if (activeWorm == null) {
            return;
        }
        Object handItem = world.getTurnManager().getTurn().getTeam().getHand();
        if (handItem == null) {
            return;

        }
        String textureName = "Hand" + handItem.toString();
        BufferedImage texture = TextureManager.WEAPONS.get(textureName);
        if (texture == null) {
            return;

        }


        AffineTransform tx = new AffineTransform();
        tx.translate(xCenter, yCenter);

        double angle = Math.atan2(mouseWorldY - yCenter, mouseWorldX - xCenter); // permet a partir d'une distance y et x entre worm et point de trouver un angle.
        tx.rotate(angle);

        if (Math.abs(angle) > Math.PI / 2) {
            tx.scale(1, -1);
        }

        tx.translate(-texture.getWidth() / 2.0, -texture.getHeight() / 2.0);
        g2d.drawImage(texture, tx, null);
    }



    private void paintMap(Graphics2D  g2d) {
        if (!initialFitDone && getWidth() > 0) {
            fitMapToScreen();
        }
        if (mapCache == null) {
            updateMapCache();
        }
        if (mapCache != null) {
            g2d.drawImage(mapCache, 0, 0, null);
        }
    }






    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //efface l'ecran

        if (this.world == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g; // pinceau plus puissant avec les translate et les zoom

        g2d.translate(cameraOffsetX, cameraOffsetY); //decalle le pinceau en fonction de la position camera (si negatif le dessin est deplacer vers la gauche)

        g2d.scale(currentZoom, currentZoom);


        paintMap(g2d);
        paintEntity(g2d);
        drawEffects(g2d);

            }
        }