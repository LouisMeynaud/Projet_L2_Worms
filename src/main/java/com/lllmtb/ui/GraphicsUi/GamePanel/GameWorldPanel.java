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
import com.lllmtb.events.TheEvents.MoveEvent;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.events.game.WorldCreatedEvent;
import com.lllmtb.events.move.MouseWheelMoveInputEvent;
import com.lllmtb.events.move.MouseWheelMoveInputEvent;
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
    public double tileSize; // sera la taille des bloc et la tailel de reference pour tout le reste. poura etre choisie
    private final double defaultTileSize = 4;
    private Map<Tile, BufferedImage> textureMatrixMap;
    private Map<String, BufferedImage> textureEntityMap;
    private final Set<Integer> pressedKeys = new HashSet<>();

    private int cameraX = 0;
    private int cameraY = 0;

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
