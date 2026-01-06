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
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.events.game.MapUpdateEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.events.input.MoveInputEvent;
import com.lllmtb.events.input.ProcessInputsEvent;
import com.lllmtb.events.input.UseInputEvent;
import com.lllmtb.events.input.JetpackBoostInputEvent;
import com.lllmtb.events.physics.ExplosionEvent;
import com.lllmtb.events.ui.graphical.RequestFocus;
import com.lllmtb.events.ui.graphical.ScreenEndGameEvent;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.ui.graphical.SoundPlayer;

public class GameWorldPanel extends JPanel {
    //--core--//
    private EventBus eventBus;
    protected GameWorld world;
    private WorldRenderer renderer;
    protected final int tileSize = 12;

    //--camera--//
    protected boolean isCameraMode = false;
    protected boolean hasDraggedCamera = false;
    protected boolean initialFitDone = false;

    protected double currentZoom = 1.0;
    protected double zoomSpeed = 0.2;

    protected double cameraOffsetX = 0;
    protected double cameraOffsetY = 0;

    private Position lastWormPos = new Position(0,  0);

    protected final Set<Integer> pressedKeys = new HashSet<>();

    private int lastMouseX = 0;
    private int lastMouseY = 0;
    protected int currentMouseX = 0;
    protected int currentMouseY = 0;
    protected double mouseWorldX = 0;
    protected double mouseWorldY = 0;

    public long chargeStartTime = 0;

    /**
     * Constructor for GameWorldPanel.
     * @param eventBus The EventBus for event handling.
     */
    public GameWorldPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        setFocusable(true);
        initWindowListeners();
        initInputListeners();
        subscribeToSystemEvents();
    }


/////////initialisation listenner (fenetre/imput)/////////////

    /**
     * Initializes window listeners for resizing and focus events.
     */
    private void initWindowListeners() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (world != null && world.getMap() != null) {
                    fitMapToScreen();
                    repaint();
                }
            }
        });

        addHierarchyListener(e -> {
            if (e.getChangeFlags() == HierarchyEvent.SHOWING_CHANGED && isShowing()) {
                requestFocusInWindow();
            }
        });
    }

    private void initInputListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (world.getPlayingTeam().isBot()) return;
                pressedKeys.add(e.getKeyCode());
                if (pressedKeys.contains(KeyEvent.VK_UP)) {
                    world.getBus().postQueued(new MoveInputEvent(Direction.Up));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        });

        MouseAdapter mouseHandler = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (world.getPlayingTeam().isBot()) return;

                lastMouseX = e.getX();
                lastMouseY = e.getY();

                if (e.getButton() == MouseEvent.BUTTON3) {
                    isCameraMode = true;
                    hasDraggedCamera = false;
                }
                if (e.getButton() == MouseEvent.BUTTON1) {
                    ItemId item = world.getPlayingTeam().getHand();
                    if (item != null) {
                        chargeStartTime = System.currentTimeMillis();
                    }
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (world.getPlayingTeam().isBot()) return;

                int type = e.getButton();
                long duration = 0;
                if (chargeStartTime > 0) {
                    duration = System.currentTimeMillis() - chargeStartTime;
                }
                chargeStartTime = 0;

                if (type == MouseEvent.BUTTON1) {
                    Position worldPos = screenToWorld(e.getX(), e.getY());
                    ItemId item = world.getPlayingTeam().getHand();

                    if (item != null) {
                         int maxTime = item.getMaxChargeTime();
                         if (maxTime > 0 && duration > maxTime) {
                             duration = maxTime;
                         }
                    }

                    if (item instanceof WeaponId && world.getPlayingTeam().getInventory().weapons.getAmmo(((WeaponId) item)) != 0) {
                        String sound = world.getPlayingTeam().getHand().toString();
                        SoundPlayer.playSound(sound + "Shot");
                    }
                    world.getBus().postQueued(new UseInputEvent(worldPos, duration));
                    repaint();
                }

                if (type == MouseEvent.BUTTON3) {
                    isCameraMode = false;
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

///////////// EVENT SUBSCRIPTIONS////////////////

    /**
     * Subscribes to system events such as world creation.
     */
    public void subscribeToSystemEvents() {

        eventBus.subscribe(WorldCreatedEvent.class, event -> {
            this.world = event.getWorld();

            if (this.world != null) {
                this.world.getBus().subscribe(ScreenEndGameEvent.class, e -> {
                    eventBus.postQueued(e);
                    this.world.stop();
                });
            }

            this.renderer = new WorldRenderer(this, this.world);
            subscribeToWorldEvents();
            this.initialFitDone = false;
            if (SoundPlayer.getCurrentMusic() == null) SoundPlayer.playMusique("MusiqueGame1");
            repaint();
        });
    }

    /**
     * Subscribes to world-specific events for updating the panel.
     */
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
            screenFollowCurrentWorms();
            repaint();
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

/////////////INPUT LOGIC (Processing)///////////////

    /**
     * Processes the current input states and posts corresponding events.
     */
    public void processInputs() {
        if (pressedKeys.contains(KeyEvent.VK_UP)) {
            world.getBus().postQueued(new JetpackBoostInputEvent());
        }
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
            world.getBus().postQueued(new MoveInputEvent(Direction.Down));
        }
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            world.getBus().postQueued(new MoveInputEvent(Direction.Left));
        }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            world.getBus().postQueued(new MoveInputEvent(Direction.Right));
        }
    }

    protected void updateMouseWorldPosition(int screenX, int screenY) {
        this.currentMouseX = screenX;
        this.currentMouseY = screenY;
        this.mouseWorldX = (currentMouseX - cameraOffsetX) / currentZoom;
        this.mouseWorldY = (currentMouseY - cameraOffsetY) / currentZoom;
    }

///////////CAMERA LOGIC//////////

    /**
     * Fits the map to the screen by adjusting zoom and camera offsets.
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
     * Checks and adjusts camera bounds to ensure it stays within the map limits.
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
     * Check that we are not trying to zoom in/out too much, and if so, reset to the maximum/minimum zoom level.
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
     * Makes the camera follow the currently active worm.
     */
    public void screenFollowCurrentWorms() {
        Worm activeWorm = world.getTurnManager().getTurn().getWormPlayable();
        PhysicsBody body = activeWorm.getBody();
        boolean hasMoved = false;
        double diffX = Math.abs(body.getPos().getX() - lastWormPos.getX());
        double diffY = Math.abs(body.getPos().getY() - lastWormPos.getY());
        if (diffX > 0.01 || diffY > 0.01) {
                hasMoved = true;
               }
        lastWormPos = body.getPos();

        if (hasMoved && !isCameraMode) {
            double targetX = body.getPos().getX() * tileSize;
            double targetY = (world.getMap().getHeight() - body.getPos().getY()) * tileSize;

            double screenCenterWidth = getWidth() / 2.0;
            double screenCenterHeight = getHeight() / 2.0;

            cameraOffsetX = screenCenterWidth - (targetX * currentZoom);
            cameraOffsetY = screenCenterHeight - (targetY * currentZoom);
            checkCameraBounds();
        }
    }

/////////////////Utilitaire/////////////////

    /**
     * Converts screen coordinates to world coordinates.
     * @param screenX The x coordinate on the screen.
     * @param screenY The y coordinate on the screen.
     * @return The corresponding Position in the game world.
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
     * Calculates the screen bounds of a physics body.
     * @param body The physics body.
     * @return An array containing the x and y screen coordinates, width, and height in pixels.
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

    protected int mapWidth() {
        return world.getMap().getWidth() * tileSize;
    }

    protected int mapHeight() {
        return world.getMap().getHeight() * tileSize;
    }

/////////////////RENDERING/////////////////

    /**
     * Paints the component by rendering the game world.
     * @param g The Graphics object for rendering.
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
