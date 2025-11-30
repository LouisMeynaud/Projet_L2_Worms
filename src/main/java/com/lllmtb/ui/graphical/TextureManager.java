package com.lllmtb.ui.graphical;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import com.lllmtb.terrain.map.Tile;

public class TextureManager {

    public static final Map<Tile, BufferedImage> TILES = new HashMap<>();
    public static final Map<String, BufferedImage> ENTITIES = new HashMap<>();
    public static final Map<String, BufferedImage> UI = new HashMap<>();

    public static void load() {
        try {

            ///////Blocs/////////
            TILES.put(Tile.DIRT, ImageIO.read(TextureManager.class.getResource("/textures/block/Dirt.png")));
            TILES.put(Tile.WATER, ImageIO.read(TextureManager.class.getResource("/textures/block/Water.png")));
            TILES.put(Tile.AIR, ImageIO.read(TextureManager.class.getResource("/textures/block/Air.png")));
            TILES.put(Tile.CAVERN, ImageIO.read(TextureManager.class.getResource("/textures/block/Cavern.png")));
            TILES.put(Tile.GRASS, ImageIO.read(TextureManager.class.getResource("/textures/block/Grass.png")));

            ///////Entite/////////
            ENTITIES.put("worm1", ImageIO.read(TextureManager.class.getResource("/textures/worm.png")));

            ///////ui-buttons/////////
            UI.put("Pause", ImageIO.read(TextureManager.class.getResource("/textures/button/Pause.png")));
            UI.put("ShootMode", ImageIO.read(TextureManager.class.getResource("/textures/button/ShootMode.png")));

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERREUR : impossible de charger une texture.");
        }
    }
}
