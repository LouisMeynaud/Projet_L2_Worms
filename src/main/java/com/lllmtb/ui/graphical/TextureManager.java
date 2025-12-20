package com.lllmtb.ui.graphical;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.model.entity.team.TeamColor;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.terrain.map.Tile;

public class TextureManager {

    public static final Map<String, BufferedImage> TILES = new HashMap<>();
    public static final Map<String, BufferedImage> ENTITIES = new HashMap<>();
    public static final Map<String, BufferedImage> UI = new HashMap<>();
    public static final Map<String, BufferedImage> WEAPONS = new HashMap<>();
    public static final Map<String, BufferedImage> TOOLS = new HashMap<>();
    public static final Map<String, BufferedImage> PROJECTILES = new HashMap<>();
    public static final Map<String, BufferedImage> BACKGROUND = new HashMap<>();
    public static final Map<String, BufferedImage> EFFECT = new HashMap<>();


    /**
     * load every texture one time at the start (avoids lag in part)
     */
    public static void load() {
        try {

            ///////Blocs/////////
            for (Tile tile : Tile.values()) {
                for (int i = 0; i < tile.getNbTexture(); i++) {
                    TILES.put(tile.toString() + i, ImageIO.read(TextureManager.class.getResource("/textures/block/" + tile.toString() + i + ".png")));
                }
            }

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
            for (TeamColor color: TeamColor.values()) {
            ENTITIES.put("Worm" + color.toString(), ImageIO.read(TextureManager.class.getResource("/sprites/worm/WormRight" + color.toString() + ".png")));
            }

            //////Arme//////////
            for (WeaponId weapon: WeaponId.values()) {
                if (weapon != WeaponId.NONE) {
                    WEAPONS.put(weapon.toString(), ImageIO.read(TextureManager.class.getResource("/textures/weapon/weapon/" + weapon.toString() + ".png")));
                    WEAPONS.put("Locked" + weapon.toString(), ImageIO.read(TextureManager.class.getResource("/textures/weapon/lockedWeapon/" + weapon.toString() + ".png")));
                    WEAPONS.put("Ammo" + weapon.toString(), ImageIO.read(TextureManager.class.getResource("/textures/weapon/ammo/" + weapon.toString() + ".png")));
                    WEAPONS.put("Hand" + weapon.toString(), ImageIO.read(TextureManager.class.getResource("/sprites/weapon/" + weapon.toString() + ".png")));
                }
            }

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

