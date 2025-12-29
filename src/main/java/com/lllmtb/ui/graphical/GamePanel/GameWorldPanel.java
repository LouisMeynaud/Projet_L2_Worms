package com.lllmtb.ui.graphical.GamePanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashSet;
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
import com.lllmtb.events.move.JetpackBoostEvent;
import com.lllmtb.events.physics.ExplosionEvent;
import com.lllmtb.events.ui.MoveEvent;
import com.lllmtb.events.ui.ShootMode;
import com.lllmtb.events.ui.graphical.RequestFocus;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.ui.graphical.SoundPlayer;

public class GameWorldPanel extends JPanel {
    private EventBus eventBus; // system bus
    protected GameWorld world; //le jeux
    private WorldRenderer renderer; //la classe qui s'occupe des dessins
    protected final int tileSize = 12;
    protected final Set<Integer> pressedKeys = new HashSet<>();
     protected boolean shootMode;

    protected boolean isCameraMode = false; //indique si l'utilisateur est entrain de bouger la camera (clique droit maintenue)
    protected boolean hasDraggedCamera = false; //indique si la camera a ete deplacer depuis le dernier clique
    protected boolean initialFitDone = false; //indique si le premier ajustement de la camera une fois la map faite a été fait
    protected double currentZoom = 1.0; //le zoom
    protected double zoomSpeed = 0.2; //la force de zoom

    protected double cameraOffsetX = 0; //decallage horizontale de la camera
    protected double cameraOffsetY = 0; //decallage verticale de la camera
    private int lastMouseX = 0; //ancienne position X de la souris
    private int lastMouseY = 0; //ancienne position Y de la souris
    protected int currentMouseX = 0; //position X de la souris
    protected int currentMouseY = 0; //position Y de la souris
    protected double mouseWorldX = 0; //position X de la souris en coordonée physique
    protected double mouseWorldY = 0; //position Y de la souris en coordonnée physique


    /**
     * initialise les listenner d'evenement et d'input
     * @param eventBus le bus systeme
     */
    public GameWorldPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        setFocusable(true);
        subscribeToSystemEvents();


        //////////////////imput///////////////////////
        //pour redessiner le monde apres un changement de taille d'ecran
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (world != null && world.getMap() != null) {
                    fitMapToScreen();
                    repaint();
                }
            }
        });

        //pour eviter de devoir cliquer sur la fenetre pour prendre le focus apres un rezise
        addHierarchyListener(e -> {
            if (e.getChangeFlags() == HierarchyEvent.SHOWING_CHANGED && isShowing()) {
                requestFocusInWindow();
            }
        });

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
                    updateMouseWorldPosition(e.getX(), e.getY());

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

                    validateZoom();

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
                updateMouseWorldPosition(e.getX(), e.getY());
                repaint();
            }
        };

        addMouseListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }


    //////////////////utilitaire////////////////////

    /**
     * convertit les coordonée a l'ecran en coordonée physique
     * @param screenX coordonée X a l'ecran
     * @param screenY coordonée Y a l'ecran
     * @return la position correspondante en coordonée physique
     */
    protected Position screenToWorld(int screenX, int screenY) {
        double pixelX = (screenX - cameraOffsetX) / currentZoom;
        double pixelY = (screenY - cameraOffsetY) / currentZoom;

        double tileX = pixelX / tileSize;
        double tileYRaw = pixelY / tileSize;

        double mapHeightTiles = world.getMap().getHeight();
        double tileY = mapHeightTiles - tileYRaw;

        return new Position(tileX, tileY);
    }

    /**
     * calcule les coordonnées d'affichage et la taille a l'ecran d'un element physique
     * convertit mes coordonée physique en coordonée a l'ecran
     * @param body le body d'on on veut calculer les coordonée
     * @return un tableau contenant [xEcran , yEcran , largeurPx , Hauteur Px]
     */
    protected double[] getScreenBounds(PhysicsBody body) {
        double largeurPx = body.getWidth() * tileSize;
        double hauteurPx = body.getHeight() * tileSize;

        double xEcran = body.getPos().getX() * tileSize;
        double yPhysique = body.getPos().getY() * tileSize;

        double hauteurMapPixels = world.getMap().getHeight() * this.tileSize;
        double yEcran = hauteurMapPixels - yPhysique - hauteurPx;

        return new double[] {xEcran, yEcran, largeurPx, hauteurPx };
    }

    /**
     * verifie que l'on essaye pas de trop zoomer/dezoomer et si c'est le cas on replace au zoomMax/minim
     */
    protected void validateZoom() {
        double screenWidth = getWidth();
        double screenHeight = getHeight();

        double minZoom = Math.max(screenWidth / mapWidth(), screenHeight / mapHeight());
        if (currentZoom < minZoom) {
            currentZoom = minZoom;
        }
        if (currentZoom > 8.0) {
            currentZoom = 8.0;
        }
    }

    /**
     * met a jour les coordonee de position de la souris (ecran et monde)
     * @param screenX la coordonne X a l'ecran
     * @param screenY la coordonne Y a l'ecran
     */
    protected void updateMouseWorldPosition(int screenX, int screenY) {
        this.currentMouseX = screenX;
        this.currentMouseY = screenY;
        this.mouseWorldX = (currentMouseX - cameraOffsetX) / currentZoom;
        this.mouseWorldY = (currentMouseY - cameraOffsetY) / currentZoom;
    }

    /**
     * retourne la largeur de la map multiplier par la taille d'une tuile
     */
    protected int mapWidth() {
        return world.getMap().getWidth() * tileSize;
    }

    /**
     * retourne la huateur de la map multiplier par la taille d'une tuile
     */
    protected int mapHeight() {
        return world.getMap().getHeight() * tileSize;
    }

    /**
     * Process pressed keys and send movement events.
     * This method should be called every game tick for smooth continuous movement.
     */
    public void processInputs() {
        if (pressedKeys.contains(KeyEvent.VK_UP)) {
            world.getBus().postQueued(new JetpackBoostEvent());
        }
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

    /**
     * s'abonne au evenement du jeu (mise a jour map , créeation monde etc...)
     */
    public void subscribeToSystemEvents() {
        eventBus.subscribe(WorldCreatedEvent.class, event -> {
            this.world = event.getWorld();
            this.renderer = new WorldRenderer(this, this.world);
            subscribeToWorldEvents();
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
            if (renderer != null) renderer.clearMapCache();
            repaint();
        });
        world.getBus().subscribe(GameStateUpdatedEvent.class, event -> {
            if (renderer != null) {
                renderer.updateAnimations();
                renderer.updateEffects();
            }
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
            if (renderer != null) {
                renderer.addEffect(event.getAnimationKey(), event.getRadius(), event.getPosition());
            }
            System.out.println(event.getAnimationKey());
            SoundPlayer.playSound(event.getAnimationKey());
        });

    }

    /**
     * ajuste la position de la camera pour que la carte tienne entiere dans l'ecran lors du premier affichage
     * appelee a l'initialisation
     */
    protected void fitMapToScreen() {
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

    /**
     * verifie et corrige la position de la camera pour qu'elle ne sorte pas de la map (pour eviter de voir des bord blanc)
     */
     protected void checkCameraBounds() {
        double screenWidth = getWidth();
        double screenHeight = getHeight();

        double mapRealWidth = mapWidth();
        double mapRealHeight = mapHeight();
        validateZoom();

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

    /**
     * appelle le render de WorldRender pour tout dessiner
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //efface l'ecran

        if (this.world == null) {
            return;
        }

        renderer.render((Graphics2D) g);
    }
}