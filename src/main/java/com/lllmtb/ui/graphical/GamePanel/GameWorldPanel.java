package com.lllmtb.ui.graphical.GamePanel;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.graphicsUi.*;
import com.lllmtb.events.move.MouseInputEvent;
import com.lllmtb.events.move.MouseWheelMoveInputEvent;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.events.game.MapUpdateEvent;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.map.Tile;

public class GameWorldPanel extends JPanel {
    public EventBus eventBus;
    public GameWorld world;
    public Tile[][] mapMatrix;
    public double tileSize;
    private final double defaultTileSize = 4;
    private Map<Tile, BufferedImage> textureMatrixMap;
    private Map<String, BufferedImage> textureEntityMap;
    private final Set<Integer> pressedKeys = new HashSet<>();

   
    private BufferedImage mapCache = null; 
    private int lastWindowWidth = -1;
    private int lastWindowHeight = -1;

    
    private boolean isCameraMode = false;
    private boolean hasDraggedCamera = false;
    private double currentZoom = 1.0;     
    private double zoomSpeed = 0.1;
    
    private double cameraOffsetX = 0;
    private double cameraOffsetY = 0;
    private int lastMouseX = 0; 
    private int lastMouseY = 0;
    

    public GameWorldPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        setFocusable(true);
        loadTextures();
        subscribeToGameEvents();
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
                    eventBus.postQueued(new MoveEvent(Direction.Up));
                }
                if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
                    eventBus.postQueued(new MoveEvent(Direction.Down));
                }
                if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
                    eventBus.postQueued(new MoveEvent(Direction.Left));
                }
                if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                    eventBus.postQueued(new MoveEvent(Direction.Right));
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
                int type = e.getButton() ;
                long duration = System.currentTimeMillis() - pressStartTime;
                double worldX = e.getX();
                double worldY = e.getY();
                if (type == MouseEvent.BUTTON1) {
                    eventBus.postQueued(new MouseInputEvent(new Position(worldX, worldY),duration, Direction.Left));
                }
                
                if (type == MouseEvent.BUTTON3) {
                    isCameraMode = false;
                    if (!hasDraggedCamera) {
                        eventBus.postQueued(new MouseInputEvent(new Position(worldX, worldY),duration, Direction.Right));
                    }
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isCameraMode) {
                    int currentX = e.getX();
                    int currentY = e.getY();

                    double dx = currentX - lastMouseX;
                    double dy = currentY - lastMouseY;

                    
                        hasDraggedCamera = true;
                        cameraOffsetX += dx;
                        cameraOffsetY += dy;
                        lastMouseX = currentX;
                        lastMouseY = currentY;

                        checkCameraBounds(); 
                        repaint();
                    
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation(); 

                if (isCameraMode) {
                    if (rotation < 0) currentZoom += zoomSpeed; 
                    else currentZoom -= zoomSpeed; 
                    
                    checkCameraBounds(); 
                    repaint(); 
                } else {
                    if (rotation < 0) {
                        eventBus.postQueued(new MouseWheelMoveInputEvent(Direction.Down));
                    }
                    if (rotation > 0) {
                        eventBus.postQueued(new MouseWheelMoveInputEvent(Direction.Up));
                    }
                }
            }
        };

        addMouseListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
        addMouseMotionListener(mouseHandler); 
        
    }


    private void checkCameraBounds() {
        double windowWidth = getWidth();
        double windowHeight = getHeight();

        //limite zoom
        if (currentZoom < 1.0) {
            currentZoom = 1.0;
        }
        if (currentZoom > 5.0) {
            currentZoom = 5.0;
        }
        
        double limitX = (windowWidth / 2.0) * (currentZoom - 1);
        double limitY = (windowHeight / 2.0) * (currentZoom - 1);

        
        if (cameraOffsetX > limitX) cameraOffsetX = limitX;
        if (cameraOffsetX < -limitX) cameraOffsetX = -limitX;

        if (cameraOffsetY > limitY) cameraOffsetY = limitY;
        if (cameraOffsetY < -limitY) cameraOffsetY = -limitY;
    }

   
    private void loadTextures() {
        textureMatrixMap = new HashMap<>();
        textureEntityMap = new HashMap<>();
        try {

            textureMatrixMap.put(Tile.DIRT, ImageIO.read(GameWorldPanel.class.getResource("/sprites/block/Dirt.png"))); // le / designe le dossier ressource
            textureMatrixMap.put(Tile.WATER, ImageIO.read(GameWorldPanel.class.getResource("/sprites/block/Water.png")));
            textureMatrixMap.put(Tile.AIR, ImageIO.read(GameWorldPanel.class.getResource("/sprites/block/Air.png")));
            textureMatrixMap.put(Tile.CAVERN, ImageIO.read(GameWorldPanel.class.getResource("/sprites/block/Cavern.png")));
            textureMatrixMap.put(Tile.GRASS, ImageIO.read(GameWorldPanel.class.getResource("/sprites/block/Grass.png")));


            textureEntityMap.put("warm1", ImageIO.read(GameWorldPanel.class.getResource("/textures/warm.png")));

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERREUR: Impossible de charger les textures !");
        }
    }

    public void subscribeToGameEvents() {

        eventBus.subscribe(WorldCreatedEvent.class, event -> {
            this.world = event.getWorld();
            this.mapMatrix = this.world.getMap().getMapMatrix();
            this.mapCache = null; //on efface l'encienne image
            repaint(); 
        });
        eventBus.subscribe(MapUpdateEvent.class, event -> {
            this.mapMatrix = this.world.getMap().getMapMatrix();
            this.mapCache = null; 
            repaint();
        });
        eventBus.subscribe(GameStateUpdatedEvent.class, event -> {
            repaint();
        });
    }

    private void updateMapCache(int windowWidth, int windowHeight) {

        mapCache = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mapCache.createGraphics(); //cree un crayon dans mapCache

        double mapTilesX = world.getMap().getWidth();
        double mapTilesY = world.getMap().getHeight();

        double widthRatio = windowWidth / mapTilesX;
        double heightRatio = windowHeight / mapTilesY;
        double sizeToFillScreen = Math.max(widthRatio, heightRatio);
        this.tileSize = Math.max(defaultTileSize, sizeToFillScreen);

        // On garde la hauteur totale pour aligner le bas de la map au bas de l'écran
        double totalMapHeightPixels = mapTilesY * this.tileSize;
        double offScreenY = windowHeight - totalMapHeightPixels; 

        int drawSizeInt = (int) Math.ceil(this.tileSize);
        g2d.fillRect(0, 0, windowWidth, windowHeight);

        for (int y = 0; y < mapMatrix.length; y++) {
            for (int x = 0; x < mapMatrix[y].length; x++) {
                Tile tileType = mapMatrix[y][x];
                
                BufferedImage texture = textureMatrixMap.get(tileType);
                if (texture != null) {
                    int screenX = (int) (x * tileSize);
                    int screenY = (int) ((y * tileSize) + offScreenY);
                    g2d.drawImage(texture, screenX, screenY, drawSizeInt, drawSizeInt, null);
                    
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //efface l'ecran

        if (this.world == null) { return; }

        int currentWidth = getWidth();//largeur fenetre
        int currentHeight = getHeight();

        if (mapCache == null) {//si on change la taille de la fenetre alors on redessine la carte 
            updateMapCache(currentWidth, currentHeight);
            lastWindowWidth = currentWidth;
            lastWindowHeight = currentHeight;
        }

        Graphics2D g2d = (Graphics2D) g; // pinceau plus puissant avec les translate et les zoom
        AffineTransform oldTransform = g2d.getTransform(); //sauvegarde l'etat initial du pinceaux , sans translate ou autre

        g2d.translate(cameraOffsetX, cameraOffsetY);//decalle le pinceau en fonction de la position camera (si negatif le dessin est deplacer vers la gauche)
        
        //pour le zoom , on deplace le pinceaux au milieu , zoom/dezoom(ver le milieu) puis remet le pinceau au bon endroit
        g2d.translate(currentWidth / 2.0, currentHeight / 2.0);
        g2d.scale(currentZoom, currentZoom);
        g2d.translate(-currentWidth / 2.0, -currentHeight / 2.0);

        
        //on dessine
        if (mapCache != null) {
            g2d.drawImage(mapCache, 0, 0, null);
        }

        
        
        BufferedImage textureWorm = textureEntityMap.get("warm1");
        double hauteurMapPixels = world.getMap().getHeight() * this.tileSize;
        double videEnHaut = currentHeight - hauteurMapPixels;

        if (world.getPhysics() != null && world.getPhysics().getBodies() != null) {
            for (PhysicsBody warm : world.getPhysics().getBodies()) {
                double largeurVerPx = warm.getWidth() * tileSize;
                double hauteurVerPx = warm.getHeight() * tileSize;
                
                double xEcran = warm.getPos().getX() * tileSize;
                double yPhysique = warm.getPos().getY() * tileSize;
                double yInverse = hauteurMapPixels - yPhysique - hauteurVerPx;// -hauteurVerPx car dessin commence en haut a gauche

                double yEcran = yInverse + videEnHaut;//ajout du decallage pour s'alligner

                
                AffineTransform tx = new AffineTransform();
                tx.translate(xEcran, yEcran); // On place le crayon
                
                //scale l'image du worms pour qu'elle soit a la bonne taille.
                double echelleX = largeurVerPx / textureWorm.getWidth();
                double echelleY = hauteurVerPx / textureWorm.getHeight();
                tx.scale(echelleX, echelleY);
                g2d.drawImage(textureWorm, tx, null);
            }
        }

        g2d.setTransform(oldTransform);
    }
}