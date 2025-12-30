package com.lllmtb.ui.graphical;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.lllmtb.engine.logger.Logger;
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
            UI.put("Coin", ImageIO.read(TextureManager.class.getResource("/textures/Coin.png")));
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
            PROJECTILES.put("BowProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/projectile/BowProjectile.png")));
            PROJECTILES.put("GrenadeProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/projectile/GrenadeProjectile.png")));
            PROJECTILES.put("CatConcreteProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/projectile/CatConcreteProjectile.png")));
            PROJECTILES.put("TowerProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/projectile/TowerProjectile.png")));
            PROJECTILES.put("BabyTowerProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/projectile/BabyTowerProjectile.png")));
            PROJECTILES.put("BulletProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/projectile/BulletGun.png")));
            PROJECTILES.put("RocketProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/projectile/BulletBazooka.png")));
            PROJECTILES.put("MissileProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/projectile/Missile.png")));
            PROJECTILES.put("HealingProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/projectile/BulletHealingBazooka.png")));
            PROJECTILES.put("ShotGunProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/projectile/ShotGunProjectile.png")));
            PROJECTILES.put("ElectronProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/projectile/ElectronProjectile.png")));
            PROJECTILES.put("GasProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/projectile/GasProjectile.png")));
            PROJECTILES.put("MineProjectile", ImageIO.read(TextureManager.class.getResource("/sprites/projectile/MineProjectile.png")));


            /////////////EFFECT/////////////////
            EFFECT.put("BazookaExplosion", ImageIO.read(TextureManager.class.getResource("/sprites/effect/BazookaExplosion.png")));
            EFFECT.put("HealingBazookaExplosion", ImageIO.read(TextureManager.class.getResource("/sprites/effect/HealingBazookaExplosion.png")));
            EFFECT.put("MissileExplosion", ImageIO.read(TextureManager.class.getResource("/sprites/effect/MissileExplosion.png")));


        } catch (IOException e) {
            Logger.logger.error("Error while loading textures : ", e);
        }
    }
}
