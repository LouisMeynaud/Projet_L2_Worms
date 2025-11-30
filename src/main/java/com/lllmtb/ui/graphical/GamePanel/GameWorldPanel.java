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
import com.lllmtb.events.move.MouseInputEvent;
import com.lllmtb.events.move.MouseWheelMoveInputEvent;
import com.lllmtb.events.ui.*;
import com.lllmtb.events.game.GamePauseEvent;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.events.game.MapUpdateEvent;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.map.Tile;

public class GameWorldPanel extends JPanel {
    private EventBus eventBus;
    private GameWorld world;
    private Tile[][] mapMatrix;
    private int tileSize = 12;
    private boolean shootMode;
    private Map<Tile, BufferedImage> textureMatrixMap;
    private Map<String, BufferedImage> textureEntityMap;
    private final Set<Integer> pressedKeys = new HashSet<>();

   
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
    

    public GameWorldPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        setFocusable(true);
        loadTextures();
        subscribeToGameEvents();
        
        
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
                double worldX = (e.getX() - cameraOffsetX) / currentZoom;
                double worldY = (e.getY() - cameraOffsetY) / currentZoom;
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
                    currentMouseX = e.getX();
                    currentMouseY = e.getY();

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

                    if (rotation < 0) currentZoom += zoomSpeed; 
                    else currentZoom -= zoomSpeed; 
                    
                    double minZoom = Math.max(getWidth() / mapWidth(),getHeight() / mapHeight());
                    if (currentZoom < minZoom) currentZoom = minZoom;
                    if (currentZoom > 8.0) currentZoom = 8.0;

                    cameraOffsetX = centerX - (mapPointX * currentZoom);
                    cameraOffsetY = centerY - (mapPointY * currentZoom);

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


            @Override
            public void mouseMoved(MouseEvent e) {
                currentMouseX = e.getX();
                currentMouseY = e.getY();
                repaint(); 
            }
        };

        addMouseListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
        addMouseMotionListener(mouseHandler); 
    }

    //////////////////utilitaire//////////////////:
    private int mapWidth() {
        return world.getMap().getWidth() * tileSize;
    }

    private int mapHeight() {
        return world.getMap().getHeight() * tileSize;
    }


    public void subscribeToGameEvents() {

        eventBus.subscribe(WorldCreatedEvent.class, event -> {
            this.world = event.getWorld();
            this.mapMatrix = this.world.getMap().getMapMatrix();
            this.mapCache = null; //on efface l'encienne image
            this.initialFitDone = false; 
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

        eventBus.subscribe(ShootMode.class, event -> {
            this.shootMode = !this.shootMode;
        });

        eventBus.subscribe(GamePauseEvent.class, event -> {
    
            if (!event.isPaused) {
                this.requestFocusInWindow(); 
            }
        });
    }

    

     private void loadTextures() {
        textureMatrixMap = new HashMap<>();
        textureEntityMap = new HashMap<>();
        try {

            textureMatrixMap.put(Tile.DIRT, ImageIO.read(GameWorldPanel.class.getResource("/textures/block/Dirt.png"))); // le / designe le dossier ressource
            textureMatrixMap.put(Tile.WATER, ImageIO.read(GameWorldPanel.class.getResource("/textures/block/Water.png")));
            textureMatrixMap.put(Tile.AIR, ImageIO.read(GameWorldPanel.class.getResource("/textures/block/Air.png")));
            textureMatrixMap.put(Tile.CAVERN, ImageIO.read(GameWorldPanel.class.getResource("/textures/block/Cavern.png")));
            textureMatrixMap.put(Tile.GRASS, ImageIO.read(GameWorldPanel.class.getResource("/textures/block/Grass.png")));


            textureEntityMap.put("worm1", ImageIO.read(GameWorldPanel.class.getResource("/textures/worm.png")));

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERREUR: Impossible de charger les textures !");
        }
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
        if (mapCache == null) return;

        double screenWidth = getWidth();
        double screenHeight = getHeight();
        
        double mapRealWidth = mapWidth();
        double mapRealHeight = mapHeight();

        double minZoom = Math.max(screenWidth / mapRealWidth, screenHeight / mapRealHeight); 
        if (currentZoom < minZoom) 
            {currentZoom = minZoom;}
        if (currentZoom > 8.0) 
            {currentZoom = 8.0;}

        double zoomedWidth = mapRealWidth * currentZoom;
        double zoomedHeigth = mapRealHeight* currentZoom;

        double minX = screenWidth - zoomedWidth;
        if (cameraOffsetX > 0) cameraOffsetX = 0;
        if (cameraOffsetX < minX) cameraOffsetX = minX;

        double minY = screenHeight- zoomedHeigth;
        if (cameraOffsetY > 0) cameraOffsetY = 0;
        if (cameraOffsetY < minY) cameraOffsetY = minY;
    }

   
    private void updateMapCache() {
        int width = mapWidth();
        int height = mapHeight();

        mapCache = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mapCache.createGraphics(); //cree un crayon dans mapCache

        g2d.setColor(new Color(135, 206, 235)); 
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
        
        if (!initialFitDone && getWidth() > 0) {
            fitMapToScreen();
        }
        checkCameraBounds();
    }

    //////////////dessins//////////////////////////://
    
    private void paintEntity(Graphics2D g2d){
        BufferedImage textureWorm = textureEntityMap.get("worm1");
        double hauteurMapPixels = world.getMap().getHeight() * this.tileSize;

        if (world.getPhysics() != null && world.getPhysics().getBodies() != null) {
            for (PhysicsBody worm : world.getPhysics().getBodies()) {
                double largeurVerPx = worm.getWidth() * tileSize;
                double hauteurVerPx = worm.getHeight() * tileSize;
                
                double xEcran = worm.getPos().getX() * tileSize;
                double yPhysique = worm.getPos().getY() * tileSize;
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
    }

    private void paintAim(Graphics2D g2d){
        Worm currentWorm = world.getTurnManager().getTurn().getWormPlayable();
        double hauteurMapPixels = world.getMap().getHeight() * this.tileSize;
        if (currentWorm != null && shootMode) { 
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3)); // epaisseur du trait
        double verLargeurPx = currentWorm.getBody().getWidth()* tileSize;
        double verHauteurPx = currentWorm.getBody().getHeight() * tileSize;
        double startX = currentWorm.getBody().getPos().getX() * tileSize + (verLargeurPx / 2.0);
        double startY = (hauteurMapPixels - (currentWorm.getBody().getPos().getY() * tileSize) - verHauteurPx) + (verHauteurPx / 2.0);
        
        double mouseWorldX = (currentMouseX - cameraOffsetX) / currentZoom;
        double mouseWorldY = (currentMouseY - cameraOffsetY) / currentZoom;

        double vecX = mouseWorldX - startX;
        double vecY = mouseWorldY - startY;

        double distance = Math.sqrt(vecX * vecX + vecY * vecY);
        double maxLen = 100.0; 

        if (distance > maxLen) {
            double ratio = maxLen / distance;
            vecX = vecX * ratio;
            vecY = vecY * ratio;
        }
        g2d.drawLine((int)startX, (int)startY, (int)(startX + vecX), (int)(startY + vecY));


        }
    }

    private void paintMap(Graphics2D  g2d){
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

        if (this.world == null) { return; }


        Graphics2D g2d = (Graphics2D) g; // pinceau plus puissant avec les translate et les zoom
        g2d.translate(cameraOffsetX, cameraOffsetY);//decalle le pinceau en fonction de la position camera (si negatif le dessin est deplacer vers la gauche)
        g2d.scale(currentZoom, currentZoom);


        paintMap(g2d);
        paintEntity(g2d);
        paintAim(g2d);
    }
}