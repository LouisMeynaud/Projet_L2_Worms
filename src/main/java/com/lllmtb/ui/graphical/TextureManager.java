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
    public static final Map<String, BufferedImage> WEAPONS = new HashMap<>();
    public static final Map<String, BufferedImage> PROJECTILES = new HashMap<>();

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

            //////Arme//////////
            WEAPONS.put("GUN", ImageIO.read(TextureManager.class.getResource("/textures/weapon/weapon/Gun.png")));
            WEAPONS.put("LockedGUN", ImageIO.read(TextureManager.class.getResource("/textures/weapon/lockedWeapon/Gun.png")));
            WEAPONS.put("AmmoGUN", ImageIO.read(TextureManager.class.getResource("/textures/weapon/ammo/Gun.png")));

            WEAPONS.put("BAZOOKA", ImageIO.read(TextureManager.class.getResource("/textures/weapon/weapon/Bazooka.png")));
            WEAPONS.put("LockedBAZOOKA", ImageIO.read(TextureManager.class.getResource("/textures/weapon/lockedWeapon/Bazooka.png")));
            WEAPONS.put("AmmoBAZOOKA", ImageIO.read(TextureManager.class.getResource("/textures/weapon/ammo/Bazooka.png")));

            WEAPONS.put("HandGUN", ImageIO.read(TextureManager.class.getResource("/sprites/weapon/GunRight.png")));
            WEAPONS.put("HandBAZOOKA", ImageIO.read(TextureManager.class.getResource("/sprites/weapon/BazookaRight.png")));

            ///////////projectile//////////
            PROJECTILES.put("BulletProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/weapon/BulletGun.png")));
            PROJECTILES.put("RocketProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/weapon/BulletGun.png")));



        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERREUR : impossible de charger une texture.");
        }
    }

}

