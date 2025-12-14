package com.lllmtb.ui.graphical;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.lllmtb.engine.logger.Logger;

public class TextureManager {

    public static final Map<String, BufferedImage> TILES = new HashMap<>();
    public static final Map<String, BufferedImage> ENTITIES = new HashMap<>();
    public static final Map<String, BufferedImage> UI = new HashMap<>();
    public static final Map<String, BufferedImage> WEAPONS = new HashMap<>();
    public static final Map<String, BufferedImage> TOOLS = new HashMap<>();
    public static final Map<String, BufferedImage> PROJECTILES = new HashMap<>();
    public static final Map<String, BufferedImage> BACKGROUND = new HashMap<>();
    public static final Map<String, BufferedImage> EFFECT = new HashMap<>();


    public static void load() {
        try {

            ///////Blocs/////////
            TILES.put("DIRT0".toString(), ImageIO.read(TextureManager.class.getResource("/textures/block/Dirt0.png")));
            TILES.put("DIRT1".toString(), ImageIO.read(TextureManager.class.getResource("/textures/block/Dirt1.png")));
            TILES.put("DIRT2".toString(), ImageIO.read(TextureManager.class.getResource("/textures/block/Dirt2.png")));
            TILES.put("DIRT3".toString(), ImageIO.read(TextureManager.class.getResource("/textures/block/Dirt3.png")));
            TILES.put("WATER0".toString(), ImageIO.read(TextureManager.class.getResource("/textures/block/Water0.png")));
            TILES.put("WATER1".toString(), ImageIO.read(TextureManager.class.getResource("/textures/block/Water1.png")));
            TILES.put("AIR0".toString(), ImageIO.read(TextureManager.class.getResource("/textures/block/Air0.png")));
            TILES.put("CAVERN0".toString(), ImageIO.read(TextureManager.class.getResource("/textures/block/Cavern0.png")));
            TILES.put("GRASS0".toString(), ImageIO.read(TextureManager.class.getResource("/textures/block/Grass0.png")));
            TILES.put("GRASS1".toString(), ImageIO.read(TextureManager.class.getResource("/textures/block/Grass1.png")));
            TILES.put("UNDERDIRT0".toString(), ImageIO.read(TextureManager.class.getResource("/textures/block/UnderDirt0.png")));

            ///////ui-buttons/////////
            UI.put("Pause", ImageIO.read(TextureManager.class.getResource("/textures/button/Pause.png")));
            UI.put("PauseHover", ImageIO.read(TextureManager.class.getResource("/textures/button/Pause.png")));
            UI.put("InventoryButton", ImageIO.read(TextureManager.class.getResource("/textures/button/InventoryButton.png")));
            UI.put("InventoryButtonHover", ImageIO.read(TextureManager.class.getResource("/textures/button/InventoryButton.png")));
            UI.put("SkipTurn", ImageIO.read(TextureManager.class.getResource("/textures/button/SkipTurn.png")));
            UI.put("SkipTurnHover", ImageIO.read(TextureManager.class.getResource("/textures/button/SkipTurn.png")));
            UI.put("ShootMode", ImageIO.read(TextureManager.class.getResource("/textures/button/ShootMode.png")));
            UI.put("ShootModeHover", ImageIO.read(TextureManager.class.getResource("/textures/button/ShootMode.png")));
            UI.put("Panel", ImageIO.read(TextureManager.class.getResource("/textures/button/Panel.png")));
            UI.put("PanelHover", ImageIO.read(TextureManager.class.getResource("/textures/button/Panel.png")));

            ///////Background/////////
            BACKGROUND.put("Background", ImageIO.read(TextureManager.class.getResource("/textures/Background.png")));

            //////Entities//////////
            ENTITIES.put("Worm", ImageIO.read(TextureManager.class.getResource("/sprites/worm/WormRight.png")));

            //////Arme//////////
            WEAPONS.put("GUN", ImageIO.read(TextureManager.class.getResource("/textures/weapon/weapon/Gun.png")));
            WEAPONS.put("LockedGUN", ImageIO.read(TextureManager.class.getResource("/textures/weapon/lockedWeapon/Gun.png")));
            WEAPONS.put("AmmoGUN", ImageIO.read(TextureManager.class.getResource("/textures/weapon/ammo/Gun.png")));

            WEAPONS.put("BAZOOKA", ImageIO.read(TextureManager.class.getResource("/textures/weapon/weapon/Bazooka.png")));
            WEAPONS.put("LockedBAZOOKA", ImageIO.read(TextureManager.class.getResource("/textures/weapon/lockedWeapon/Bazooka.png")));
            WEAPONS.put("AmmoBAZOOKA", ImageIO.read(TextureManager.class.getResource("/textures/weapon/ammo/Bazooka.png")));

            WEAPONS.put("HandGUN", ImageIO.read(TextureManager.class.getResource("/sprites/weapon/GunRight.png")));
            WEAPONS.put("HandBAZOOKA", ImageIO.read(TextureManager.class.getResource("/sprites/weapon/BazookaRight2.png")));

            //////Outil//////////
            TOOLS.put("PARACHUTE", ImageIO.read(TextureManager.class.getResource("/textures/tool/tool/Parachute.png")));
            TOOLS.put("AmmoPARACHUTE", ImageIO.read(TextureManager.class.getResource("/textures/tool/ammo/Parachute.png")));

            ///////////projectile//////////
            PROJECTILES.put("BulletProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/weapon/BulletGun.png")));
            PROJECTILES.put("RocketProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/weapon/BulletBazooka.png")));

            /////////////EFFECT/////////////////
            EFFECT.put("BazookaExplosion", ImageIO.read(TextureManager.class.getResource("/sprites/effect/BazookaExplosion.png")));


        } catch (IOException e) {
            Logger.logger.error("Error while loading textures : ", e);
        }
    }

}

