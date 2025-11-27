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
import javax.swing.*;

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
    public int tileSize = 12;
    private final int defaultTileSize = 4;
    private Map<Tile, BufferedImage> textureMatrixMap;
    private Map<String, BufferedImage> textureEntityMap;
    private final Set<Integer> pressedKeys = new HashSet<>();

   
    private BufferedImage mapCache = null;
    
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
        double mWidth = mapWidth()*currentZoom;
        double mHeight = mapHeight()*currentZoom;

        double wWidth = getWidth();
        double wHeight = getHeight();

        // limite zoom
        if (currentZoom < 0.3) {
            currentZoom = 0.3;
        }
        if (currentZoom > 8.0) {
            currentZoom = 8.0;
        }

        System.out.println("X =" + cameraOffsetX + " | Y =" + cameraOffsetY);
        if (cameraOffsetX < wWidth-mWidth) cameraOffsetX = wWidth-mWidth;
        if (cameraOffsetX > 0) cameraOffsetX = 0;

        if (cameraOffsetY < wHeight-mHeight) cameraOffsetY = wHeight-mHeight;
        if (cameraOffsetY > 0) cameraOffsetY = 0;
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

    private int mapWidth() {
        int mapTilesX = world.getMap().getWidth();
        return mapTilesX * tileSize;
    }

    private int mapHeight() {
        int mapTilesY = world.getMap().getHeight();
        return mapTilesY * tileSize;
    }

    private void updateMapCache() {
        int width = mapWidth();
        int height = mapHeight();

        mapCache = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mapCache.createGraphics(); //cree un crayon dans mapCache

        g2d.fillRect(0, 0, width, height);

        for (int y = 0; y < mapMatrix.length; y++) {
            for (int x = 0; x < mapMatrix[y].length; x++) {
                Tile tileType = mapMatrix[y][x];
                
                BufferedImage texture = textureMatrixMap.get(tileType);
                if (texture != null) {
                    int screenX = x * tileSize;
                    int screenY = y * tileSize;
                    g2d.drawImage(texture, screenX, screenY, tileSize, tileSize, null);
                }
            }
        }

        g2d.dispose();

        // DEBUG - PAINT THE WHOLE TERRAIN

        // Create a JPanel to display the image
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(mapCache, 0, 0, null);  // Draw the image at position (0,0)
            }
        };

        // Create a JFrame to contain the JPanel
        JFrame frame = new JFrame("TERRAIN");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height); // Set the window size to match the image
        frame.add(imagePanel); // Add the panel that draws the image
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setVisible(true); // Show the frame
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //efface l'ecran

        if (this.world == null) { return; }

        int currentWidth = getWidth();//largeur fenetre
        int currentHeight = getHeight();

        if (mapCache == null) {//si on change la taille de la fenetre alors on redessine la carte 
            updateMapCache();
        }

        Graphics2D g2d = (Graphics2D) g; // pinceau plus puissant avec les translate et les zoom
        AffineTransform oldTransform = g2d.getTransform(); //sauvegarde l'etat initial du pinceaux , sans translate ou autre


        g2d.translate(cameraOffsetX, cameraOffsetY);//decalle le pinceau en fonction de la position camera (si negatif le dessin est deplacer vers la gauche)


        //pour le zoom , on deplace le pinceaux au milieu , zoom/dezoom(ver le milieu) puis remet le pinceau au bon endroit
        //g2d.translate(currentWidth / 2.0, currentHeight / 2.0);
        g2d.scale(currentZoom, currentZoom);
        //g2d.translate(-currentWidth / (2.0 * currentZoom), -currentZoom*currentHeight / (2.0);

        //on dessine
        if (mapCache != null) {
            g2d.drawImage(mapCache, 0, 0, null);
        }

        
        
        BufferedImage textureWorm = textureEntityMap.get("warm1");
        double hauteurMapPixels = world.getMap().getHeight() * this.tileSize;

        if (world.getPhysics() != null && world.getPhysics().getBodies() != null) {
            for (PhysicsBody warm : world.getPhysics().getBodies()) {
                double largeurVerPx = warm.getWidth() * tileSize;
                double hauteurVerPx = warm.getHeight() * tileSize;
                
                double xEcran = warm.getPos().getX() * tileSize;
                double yPhysique = warm.getPos().getY() * tileSize;
                double yEcran = hauteurMapPixels - yPhysique - hauteurVerPx;// -hauteurVerPx car dessin commence en haut a gauche

                
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