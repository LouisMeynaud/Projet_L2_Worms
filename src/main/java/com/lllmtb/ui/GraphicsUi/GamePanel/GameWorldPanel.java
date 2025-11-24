package com.lllmtb.ui.GraphicsUi.GamePanel;

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
import com.lllmtb.events.TheEvents.*;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.map.Tile;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;


public class GameWorldPanel extends JPanel {
    public EventBus eventBus;
    public GameWorld world;
    public Tile[][] mapMatrix;
    public  double tileSize; //sera la taille des bloc et la tailel de reference pour tout le reste. poura etre choisie
    private double defaultTileSize = 4;
    private Map<Tile,BufferedImage> textureMatrixMap;
    private Map<String,BufferedImage> textureEntityMap;
    private final Set<Integer> pressedKeys = new HashSet<>();

    private int cameraX = 0;
    private int cameraY = 0;
    
    public GameWorldPanel(EventBus eventBus){
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
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int type = e.getButton() ;
                long duration = System.currentTimeMillis() - pressStartTime;
                double worldX = e.getX();
                double worldY = e.getY();
                if (type == 1){
                eventBus.postQueued(new MouseInputEvent(new Position(worldX, worldY),duration, Direction.Left));
                }
                if (type == 3){
                    eventBus.postQueued(new MouseInputEvent(new Position(worldX, worldY),duration, Direction.Right));
                }
            }
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                if (rotation < 0) {
                    eventBus.postQueued(new MouseWheelMoveInputEvent(Direction.Down));
                }
                if (rotation > 0) {
                    eventBus.postQueued(new MouseWheelMoveInputEvent(Direction.Up));
                }
            }

        };

        addMouseListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
    }
        private void loadTextures() {
            textureMatrixMap = new HashMap<>();
            textureEntityMap = new HashMap<>();
        try {
            
            textureMatrixMap.put(Tile.DIRT, ImageIO.read(GameWorldPanel.class.getResource("/textures/dirt.png"))); // le / designe le dossier ressource
            textureMatrixMap.put(Tile.WATER, ImageIO.read(GameWorldPanel.class.getResource("/textures/water.png")));
            textureMatrixMap.put(Tile.AIR, ImageIO.read(GameWorldPanel.class.getResource("/textures/air.png")));

            textureEntityMap.put("warm1",ImageIO.read(GameWorldPanel.class.getResource("/textures/warm.png")));
            
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERREUR: Impossible de charger les textures !");
        }
        }

        public void subscribeToGameEvents(){

        
        eventBus.subscribe(WorldCreatedEvent.class,event -> {
            this.world = event.getWorld();
            this.mapMatrix = this.world.getMap().getMapMatrix();

            repaint(); //le premier dessins
        });
        eventBus.subscribe(GameStateUpdatedEvent.class, event -> {
        
        repaint();
    });
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g; // ceci est un "pinceau" le version graphics2d contient plus de mode par default 

        
        //trouver taille tuile pour remplir la map
        double windowWidth = getWidth();
        double windowHeight = getHeight();
        double mapTilesX = world.getMap().getWidth();
        double mapTilesY = world.getMap().getHeight();
        double widthRatio = windowWidth / mapTilesX;
        double heightRatio = windowHeight / mapTilesY;
        double sizeToFillScreen = Math.max(widthRatio, heightRatio);
        this.tileSize = Math.max(defaultTileSize, sizeToFillScreen);



        g2d.translate(0, getHeight()); // Déplace l'origine du pinceaux en bas a gauche du jpanel
    
        g2d.scale(1.0, -1.0);//inverse l'axe y , quand y descend , le pinceau descend et ne monte plus (comme il le fait initialement)
        //g2d.translate(-cameraX, -cameraY); //plutot que deplacer une camera (pas possible) on deplace le monde (dans la direction inverse ducoup)

        // Dessine la map 
        if (mapMatrix != null) {
            for (int y = 0; y < mapMatrix.length; y++) {
                for (int x = 0; x < mapMatrix[y].length; x++) {
                    Tile tileType = mapMatrix[y][x];
                    
                    int drawY = (world.getMap().getHeight() - 1) - y;// sert a inverser la ou l'on peint (car sinon par d'en haut donc peint a l'envers)
                    BufferedImage texture = textureMatrixMap.get(tileType);
                    // Calcule les coordonnées de destination en PIXELS
                    double x1 = x * tileSize;
                    double y1 = drawY * tileSize; // Le Y en bas (basé sur drawY)
                    double x2 = x * tileSize + tileSize;
                    double y2 = drawY * tileSize + tileSize; // Le Y en haut (basé sur drawY

                    g2d.drawImage(texture,(int)x1,(int) y2,(int) x2,(int) y1, 0, 0,texture.getWidth(), texture.getHeight(),null); //le null est important , c'est pour l'image observer (pas besoin car les texture cahrge assez vite)
                    
                    }
                }
            }
        BufferedImage texture = textureEntityMap.get("warm1");
        
        for(PhysicsBody warm:world.getPhysics().getBodies()){
            AffineTransform tx = new AffineTransform(); // outil permetant a drawImage de prendre des double (il prend initialement des int)
            double tailleEnPixelX = warm.getWidth() * tileSize;
            double tailleEnPixelY = warm.getHeight() * tileSize;

        
            double scaleX = tailleEnPixelX / texture.getWidth();
            double scaleY = tailleEnPixelY / texture.getHeight();
            
            // attention! toute les methode tx sont appel a l'envers (regardez pourquoi il on choisie sa et comment sa marche reelement).
            tx.translate(warm.getPos().getX() * tileSize, warm.getPos().getY() * tileSize); 
            tx.scale(scaleX, -scaleY); //on inverse l'image sur l'axe y
            tx.translate(0, -texture.getHeight()); // on deplace la texture de -sa hauteur car vue qu'elle est inverser elle sera au dessus de sont point y voulu

            g2d.drawImage(texture, tx, null);
        }
        

        // On réinitialise la caméra 
        g2d.translate(cameraX, cameraY);

    }
    }
   
