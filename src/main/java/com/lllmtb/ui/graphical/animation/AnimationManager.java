package com.lllmtb.ui.graphical.animation;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.ui.graphical.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire;

/**
 * gestionnaire des animation du jeu
 * cette classe charge et stocke les animation dans un map.
 */
public class AnimationManager {


   private static final Map<String, BufferedImage[]> FRAMES_MAP = new HashMap<>(); // le map contenant les tablea de frame pour les animation

    /**
     * charge les animation au demarage et les ajoute au FRAMES_MAP
     */
   public static void load() {

        //WORM
        BufferedImage wormGREEN = TextureManager.ENTITIES.get("WormGREEN");
        if (wormGREEN != null) {
            FRAMES_MAP.put("WormGREEN", Utilitaire.cutSpriteSheet(wormGREEN, 2, 3));
        }
        BufferedImage wormYELLOW = TextureManager.ENTITIES.get("WormYELLOW");
        if (wormYELLOW != null) {
            FRAMES_MAP.put("WormYELLOW", Utilitaire.cutSpriteSheet(wormYELLOW, 2, 3));
        }
        BufferedImage wormBLUE = TextureManager.ENTITIES.get("WormBLUE");
        if (wormBLUE != null) {
            FRAMES_MAP.put("WormBLUE", Utilitaire.cutSpriteSheet(wormBLUE, 2, 3));
        }
        BufferedImage wormRED = TextureManager.ENTITIES.get("WormRED");
        if (wormRED != null) {
            FRAMES_MAP.put("WormRED", Utilitaire.cutSpriteSheet(wormRED, 2, 3));
        }

        //EXPLOSION
        BufferedImage bazookaSheet = TextureManager.EFFECT.get("BazookaExplosion");
        if (bazookaSheet != null) {
            FRAMES_MAP.put("BazookaExplosion", Utilitaire.cutSpriteSheet(bazookaSheet, 4, 4));
        }
        BufferedImage healingBazookaSheet = TextureManager.EFFECT.get("HealingBazookaExplosion");
        if (healingBazookaSheet != null) {
            FRAMES_MAP.put("HealingBazookaExplosion", Utilitaire.cutSpriteSheet(healingBazookaSheet, 4, 4));
        }
        BufferedImage missileSheet = TextureManager.EFFECT.get("MissileExplosion");
        if (missileSheet != null) {
            FRAMES_MAP.put("MissileExplosion", Utilitaire.cutSpriteSheet(missileSheet, 4, 4));
        }
       BufferedImage mineSheet = TextureManager.EFFECT.get("MineExplosion");
       if (mineSheet != null) {
           FRAMES_MAP.put("MineExplosion", Utilitaire.cutSpriteSheet(mineSheet, 4, 4));
       }


    }

    /**
     * cree un nouvelle instance d'animation
     * @param key la cle de l'animation
     * @param speed la vitesse a laquelle on lit les frame
     * @return une nouvelle instancer d'animation prete a etre jouer
     */
    public static Animation create(String key, int speed) {
        BufferedImage[] frames = FRAMES_MAP.get(key);
        if (frames == null) {
            Logger.logger.warn("Animation not found: {}", key);
            return null;
        }
        return new Animation(frames, speed);
    }
}