package com.lllmtb.ui.graphical.animation;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.lllmtb.ui.graphical.TextureManager;
import com.lllmtb.ui.graphical.Utilitaire;

public class AnimationManager {


    private static final Map<String, BufferedImage[]> FRAMES_MAP = new HashMap<>();

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


    }


    public static Animation create(String key, int speed) {
        BufferedImage[] frames = FRAMES_MAP.get(key);
        if (frames == null) {
            System.out.println("animation inexistante : " + key);
            return null;
        }
        return new Animation(frames, speed);
    }
}