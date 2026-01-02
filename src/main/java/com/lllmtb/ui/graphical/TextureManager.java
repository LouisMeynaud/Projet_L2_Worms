package com.lllmtb.ui.graphical;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.TeamColor;
import com.lllmtb.model.item.tool.ToolId;
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
    public static final Map<String, BufferedImage> NOTIFICATIONS = new HashMap<>();


    /**
     * Loads all textures into their respective maps.
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
            UI.put("Coin", ImageIO.read(TextureManager.class.getResource("/textures/Coin.png")));
            UI.put("Drop", ImageIO.read(TextureManager.class.getResource("/textures/Drop.png")));
            UI.put("ShopButton", ImageIO.read(TextureManager.class.getResource("/textures/button/ShopButton.png")));
            UI.put("ShopButtonHover", ImageIO.read(TextureManager.class.getResource("/textures/button/ShopButton.png")));
            UI.put("ChangeWorms", ImageIO.read(TextureManager.class.getResource("/textures/button/changeWorms.png")));
            UI.put("ChangeWormsHover", ImageIO.read(TextureManager.class.getResource("/textures/button/changeWorms.png")));

            ///////Background/////////
            BACKGROUND.put("Background", ImageIO.read(TextureManager.class.getResource("/textures/Background.png")));
            BACKGROUND.put("Background2", ImageIO.read(TextureManager.class.getResource("/textures/Background2.png")));

            //////Entities//////////
            for (TeamColor color: TeamColor.values()) {
            ENTITIES.put("Worm" + color.toString(), ImageIO.read(TextureManager.class.getResource("/sprites/worm/WormRight" + color + ".png")));
            }

            //////Arme//////////
            for (WeaponId weapon: WeaponId.values()) {
                if (weapon != WeaponId.NONE) {
                    WEAPONS.put(weapon.toString(), ImageIO.read(TextureManager.class.getResource("/textures/weapon/weapon/" + weapon + ".png")));
                    WEAPONS.put("Locked" + weapon, ImageIO.read(TextureManager.class.getResource("/textures/weapon/lockedWeapon/" + weapon + ".png")));
                    WEAPONS.put("Ammo" + weapon, ImageIO.read(TextureManager.class.getResource("/textures/weapon/ammo/" + weapon + ".png")));
                    WEAPONS.put("Hand" + weapon, ImageIO.read(TextureManager.class.getResource("/sprites/weapon/" + weapon + ".png")));
                }
            }

            //////Outil//////////
            for (ToolId tool : ToolId.values()) {
                if (tool != ToolId.NONE) {
                    TOOLS.put(tool.toString(), ImageIO.read(TextureManager.class.getResource("/textures/tool/tool/" + tool + ".png")));
                    TOOLS.put("Ammo" + tool, ImageIO.read(TextureManager.class.getResource("/textures/tool/ammo/" + tool + ".png")));
                    TOOLS.put("Hand" + tool, ImageIO.read(TextureManager.class.getResource("/sprites/tool/" + tool + ".png")));
                    TOOLS.put("Equipped" + tool, ImageIO.read(TextureManager.class.getResource("/sprites/tool/" + tool + ".png")));
                }
            }

            ///////////projectile//////////
            for (Projectile.PROJECTILE projectile : Projectile.PROJECTILE.values()) {
                PROJECTILES.put(projectile.toString(), ImageIO.read(TextureManager.class.getResource("/sprites/projectile/" + projectile + ".png")));
            }

            /////////////EFFECT/////////////////
            EFFECT.put("BazookaExplosion", ImageIO.read(TextureManager.class.getResource("/sprites/effect/BazookaExplosion.png")));
            EFFECT.put("HealingBazookaExplosion", ImageIO.read(TextureManager.class.getResource("/sprites/effect/HealingBazookaExplosion.png")));
            EFFECT.put("MissileExplosion", ImageIO.read(TextureManager.class.getResource("/sprites/effect/MissileExplosion.png")));

            NOTIFICATIONS.putAll(WEAPONS);
            NOTIFICATIONS.putAll(TOOLS);
            NOTIFICATIONS.put("Skull", ImageIO.read(TextureManager.class.getResource("/textures/notification/Skull.png")));
            NOTIFICATIONS.put("Coin", ImageIO.read(TextureManager.class.getResource("/textures/notification/Coin.png")));
            NOTIFICATIONS.put("Poison", ImageIO.read(TextureManager.class.getResource("/textures/notification/Poison.png")));
            NOTIFICATIONS.put("Burn", ImageIO.read(TextureManager.class.getResource("/textures/notification/Burn.png")));
            NOTIFICATIONS.put("RisingWater", ImageIO.read(TextureManager.class.getResource("/textures/notification/RisingWater.png")));

        } catch (IOException e) {
            Logger.logger.error("Error while loading textures : ", e);
        }
    }
}
