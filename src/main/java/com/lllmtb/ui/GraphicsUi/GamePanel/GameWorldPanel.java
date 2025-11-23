package com.lllmtb.ui.GraphicsUi.GamePanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.HierarchyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import com.lllmtb.terrain.map.Tile;

public class GameWorldPanel extends JPanel {
    public EventBus eventBus;
    public GameWorld world;
    public Tile[][] mapMatrix;
    public double tileSize; // sera la taille des bloc et la tailel de reference pour tout le reste. poura etre choisie
    private final double defaultTileSize = 4;
    private Map<Tile, BufferedImage> textureMatrixMap;
    private Map<String, BufferedImage> textureEntityMap;
    private final Set<Integer> pressedKeys = new HashSet<>();

    private int cameraX = 0;
    private int cameraY = 0;

    public GameWorldPanel(EventBus eventBus) {
        this.eventBus = eventBus;
        setFocusable(true); // autorise le panel à recevoir le focus
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
                // 1. On met à jour la mémoire des touches (on ajoute celle qui vient d'être
                // pressée)
                pressedKeys.add(e.getKeyCode());

                if (pressedKeys.contains(KeyEvent.VK_UP)) {
                    eventBus.postQueued(new MoveUpEvent());
                }
                if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
                    eventBus.postQueued(new MoveDownEvent());
                }
                if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
                    eventBus.postQueued(new MoveLeftEvent());
                }
                if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                    eventBus.postQueued(new MoveRightEvent());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Important : on enlève la touche de la mémoire quand on la lâche
                pressedKeys.remove(e.getKeyCode());
            }
        });
    }

    private void loadTextures() {
        textureMatrixMap = new HashMap<>();
        textureEntityMap = new HashMap<>();
        try {

            textureMatrixMap.put(Tile.DIRT, ImageIO.read(GameWorldPanel.class.getResource("/textures/dirt.png"))); // le / designe le dossier ressource
            textureMatrixMap.put(Tile.WATER, ImageIO.read(GameWorldPanel.class.getResource("/textures/water.png")));
            textureMatrixMap.put(Tile.AIR, ImageIO.read(GameWorldPanel.class.getResource("/textures/air.png")));

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

            repaint(); // le premier dessins
        });
        eventBus.subscribe(GameStateUpdatedEvent.class, event -> {

            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if(this.world == null){return;}

        double windowWidth = getWidth();
        double windowHeight = getHeight();
        double mapTilesX = world.getMap().getWidth();
        double mapTilesY = world.getMap().getHeight();

        double widthRatio = windowWidth / mapTilesX;
        double heightRatio = windowHeight / mapTilesY;
        double sizeToFillScreen = Math.max(widthRatio, heightRatio);
        this.tileSize = Math.max(defaultTileSize, sizeToFillScreen);

        double totalMapHeightPixels = mapTilesY * this.tileSize;
        double offScreenY = windowHeight - totalMapHeightPixels; // calcule de la partit a ne pas afficher

        int drawSizeInt = (int) Math.ceil(this.tileSize); // (arrondit superieur (enleve les traits blancs)

        for (int matrixY = 0; matrixY < mapMatrix.length; matrixY++) {
            for (int matrixX = 0; matrixX < mapMatrix[matrixY].length; matrixX++) {
                Tile tileType = mapMatrix[matrixY][matrixX];
                BufferedImage texture = textureMatrixMap.get(tileType);

                int screenX = (int) (matrixX * tileSize);
                int screenY = (int) ((matrixY * tileSize) + offScreenY);

                if (!(screenY + drawSizeInt < 0 && screenY > windowHeight)) {
                    g2d.drawImage(texture, screenX, screenY, drawSizeInt, drawSizeInt, null);
                }
            }

        }
        BufferedImage textureWorm = textureEntityMap.get("warm1");

        for (PhysicsBody warm : world.getPhysics().getBodies()) {
            double entityWidthPixels = warm.getWidth() * tileSize;
            double entityHeightPixels = warm.getHeight() * tileSize;

            double xEntityPosition = warm.getPos().getX() * tileSize;
            double yEntityPosition = warm.getPos().getY() * tileSize;

            double yEntityScreenPosition = totalMapHeightPixels - yEntityPosition - entityHeightPixels;

            double xEntityScreenPosition = xEntityPosition;
            double screenY = yEntityScreenPosition + offScreenY;

            AffineTransform tx = new AffineTransform();
            tx.translate(xEntityScreenPosition, screenY);
            double scaleX = entityWidthPixels / textureWorm.getWidth();
            double scaleY = entityHeightPixels / textureWorm.getHeight();
            tx.scale(scaleX, scaleY);
            g2d.drawImage(textureWorm, tx, null);
        }

    }
}
