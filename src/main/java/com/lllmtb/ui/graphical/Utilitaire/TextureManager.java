package com.lllmtb.ui.graphical.Utilitaire;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
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

     public static BufferedImage loadImage(String path) {
        try {
            URL url = TextureManager.class.getResource(path);
            if (url == null) throw new IOException("File not found: " + path);
            return ImageIO.read(url);
        } catch (Exception e) {
            Logger.logger.warn("Error while loading textures : ", e);
            try {
                URL defaultUrl = TextureManager.class.getResource("/default.png");

                if (defaultUrl == null) throw new Exception("Default texture missing at /default.png");
                return ImageIO.read(defaultUrl);
            } catch (Exception error) {
                Logger.logger.error("Error while loading default textures : ", e);
                throw new RuntimeException("critical error: Texture " + path + " is missing and /default.png could not be loaded. Program halted.", error);
            }
        }
    }

    /**
     * Loads all textures into their respective maps.
     */
    public static void load() {

        ///////Blocs/////////
        for (Tile tile : Tile.values()) {
            for (int i = 0; i < tile.getNbTexture(); i++) {
                TILES.put(tile.toString() + i, loadImage("/textures/block/" + tile.toString() + i + ".png"));
            }
        }

        ///////ui-buttons/////////
        UI.put("Pause", loadImage("/textures/button/Pause.png"));
        UI.put("PauseHover", loadImage("/textures/button/Pause.png"));
        UI.put("InventoryButton", loadImage("/textures/button/InventoryButton.png"));
        UI.put("InventoryButtonHover", loadImage("/textures/button/InventoryButton.png"));
        UI.put("SkipTurn", loadImage("/textures/button/SkipTurn.png"));
        UI.put("SkipTurnHover", loadImage("/textures/button/SkipTurn.png"));
        UI.put("Panel", loadImage("/textures/button/Panel.png"));
        UI.put("PanelHover", loadImage("/textures/button/Panel.png"));
        UI.put("Coin", loadImage("/textures/Coin.png"));
        UI.put("DropAerials", loadImage("/textures/DropAerials.png"));
        UI.put("DropOnGround", loadImage("/textures/DropOnGround.png"));
        UI.put("ShopButton", loadImage("/textures/button/ShopButton.png"));
        UI.put("ShopButtonHover", loadImage("/textures/button/ShopButton.png"));
        UI.put("ChangeWorms", loadImage("/textures/button/changeWorms.png"));
        UI.put("ChangeWormsHover", loadImage("/textures/button/changeWorms.png"));

        ///////Background/////////
        BACKGROUND.put("Background", loadImage("/textures/Background.png"));
        BACKGROUND.put("Background2", loadImage("/textures/Background2.png"));
        BACKGROUND.put("Background3", loadImage("/textures/Background3.png"));

        //////Entities//////////
        for (TeamColor color: TeamColor.values()) {
            ENTITIES.put("Worm" + color.toString(), loadImage("/sprites/worm/WormRight" + color + ".png"));
        }

        //////Arme//////////
        for (WeaponId weapon: WeaponId.values()) {
            if (weapon != WeaponId.NONE) {
                WEAPONS.put(weapon.toString(), loadImage("/textures/weapon/weapon/" + weapon + ".png"));
                WEAPONS.put("Locked" + weapon, loadImage("/textures/weapon/lockedWeapon/" + weapon + ".png"));
                WEAPONS.put("Ammo" + weapon, loadImage("/textures/weapon/ammo/" + weapon + ".png"));
                WEAPONS.put("Hand" + weapon, loadImage("/sprites/weapon/" + weapon + ".png"));
            }
        }

        //////Outil//////////
        for (ToolId tool : ToolId.values()) {
            if (tool != ToolId.NONE) {
                TOOLS.put(tool.toString(), loadImage("/textures/tool/tool/" + tool + ".png"));
                TOOLS.put("Ammo" + tool, loadImage("/textures/tool/ammo/" + tool + ".png"));
                TOOLS.put("Hand" + tool, loadImage("/sprites/tool/" + tool + ".png"));
                TOOLS.put("Equipped" + tool, loadImage("/sprites/tool/" + tool + ".png"));
            }
        }

        ///////////projectile//////////
        for (Projectile.PROJECTILE projectile : Projectile.PROJECTILE.values()) {
            PROJECTILES.put(projectile.toString(), loadImage("/sprites/projectile/" + projectile + ".png"));
        }

        /////////////EFFECT/////////////////
        EFFECT.put("BazookaExplosion", loadImage("/sprites/effect/BazookaExplosion.png"));
        EFFECT.put("HealingBazookaExplosion", loadImage("/sprites/effect/HealingBazookaExplosion.png"));
        EFFECT.put("MissileExplosion", loadImage("/sprites/effect/MissileExplosion.png"));
        EFFECT.put("MineExplosion", loadImage("/sprites/effect/MineExplosion.png"));

        EFFECT.put("Poison", loadImage("/textures/effect/poison.png"));
        EFFECT.put("Burn", loadImage("/textures/effect/burn.png"));

        NOTIFICATIONS.putAll(WEAPONS);
        NOTIFICATIONS.putAll(TOOLS);
        NOTIFICATIONS.put("Skull", loadImage("/textures/notification/Skull.png"));
        NOTIFICATIONS.put("Coin", loadImage("/textures/notification/Coin.png"));
        NOTIFICATIONS.put("Poison", loadImage("/textures/notification/Poison.png"));
        NOTIFICATIONS.put("Burn", loadImage("/textures/notification/Burn.png"));
        NOTIFICATIONS.put("RisingWater", loadImage("/textures/notification/RisingWater.png"));
    }
}