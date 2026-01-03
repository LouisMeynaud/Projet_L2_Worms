package com.lllmtb.terrain.generation;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.MapLoader;
import com.lllmtb.terrain.map.SaveMap;


public class GenerateMap {
    public static ConfigMap world = new ConfigMap(100, 4, 5, false, 17, LandScape.HILLY, 1, 4, GameMode.NORMAL);
    public static ConfigMap flyingWorld = new ConfigMap(100, 4, 5, true, 17, LandScape.HILLY, 1, 4, GameMode.NORMAL);


    /**
     * method for generating a random map
     * @param config takes a config type object in order to generate a map according to presets
     * @return the create map
     */
    public static Map createMap(ConfigMap config) {
        try {
            Map map = new Map(config.getHeight(), config.getWidth(), SaveMap.newMapSaveFile());
            map.initializeHeightWater();
            int[] lowLine;

            int[] hightLine = LineGenerator.createLine(config);
            if (config.isFlyingIsland()) {
                lowLine = LineGenerator.createLine(config);
                triHightLineLowLine(hightLine, lowLine, config);

            } else {
                lowLine = new int[config.getWidth()];
                for (int i = 0; i < config.getWidth(); i++) {
                    lowLine[i] = config.getWaterHeight() + 1;
                }
            }


            MapBalancer.leftRightBalancing(lowLine, hightLine, config.getHeight());
            int currentAvgThickness = MapBalancer.averageThickness(lowLine, hightLine, 0, config.getWidth());

            MapBalancer.thicknessBalencer(config.getThicknessMin(), currentAvgThickness, hightLine, config.getHeight());
            if (!config.isFlyingIsland()) {
            applyIslandBorders(hightLine, lowLine, config.getWidth(), config.getWaterHeight());
            }
            applyTopSafety(hightLine, config.getHeight());
            MapFiller.fillingWater(map, config.getWaterHeight());
            MapFiller.fillingDirt(map, hightLine, lowLine, config);

            return map;
        } catch (Exception e) {
            Logger.logger.error("Erreur lors de la génération de la carte.", e);
            Logger.logger.info("Chargement de la carte par défaut.");
            return MapLoader.loadMatrix("defaultMap");
        }
    }

    /**
     * methode pour que la hightLine soit toujours au dessus de la lowLine.
     * @param hightLine la ligne haute
     * @param lowLine   la ligne basse
     * @param config    la config de la map
     */
    protected static void triHightLineLowLine(int[] hightLine, int[] lowLine, ConfigMap config) {
        try {
            for (int i = 0; i < config.getWidth(); i++) {
                if (lowLine[i] > hightLine[i]) {
                    int temp = lowLine[i];
                    lowLine[i] = hightLine[i];
                    hightLine[i] = temp;
                }
            }
        } catch (Exception e) {
            Logger.logger.error("Erreur dans triHightLineLowLine", e);
        }
    }

    // on descend les bord pour avoir un effet ile
    private static void applyIslandBorders(int[] hightLine, int[] lowLine, int width, int waterLevel) {
        try {
            int borderSize = 10;

            for (int i = 0; i < width; i++) {
                double factor = 1.0; // un factor (vaut 1 si ont est au bord , 0 si on est a borderSize du bord)
                if (i < borderSize) {
                    factor = (double) i / borderSize; // calcule le % de diminution
                } else if (i >= width - borderSize) {
                    factor = (double) (width - 1 - i) / borderSize; // calcul inverse
                }

                if (factor < 1.0) { // si on est sur une bordure , alors on baisse le terrain
                    hightLine[i] = (int) (hightLine[i] * factor);
                    if (factor < 0.2) hightLine[i] = Math.min(hightLine[i], waterLevel - 1); // si proche d'un bord , alors on eneleve le terrain
                    lowLine[i] = (int) (lowLine[i] * factor);
                }
            }
        } catch (Exception e) {
            Logger.logger.error("Erreur dans applyIslandBorders", e);
        }
    }

    private static void applyTopSafety(int[] hightLine, int mapHeight) {
        try {
            // limite de hauteur (pour pas etre trop proche du plafond et etre coincee)
            int limit = mapHeight - 8;

            for (int i = 0; i < hightLine.length; i++) {
                if (hightLine[i] > limit) {
                    hightLine[i] = limit;
                }
            }
        } catch (Exception e) {
            Logger.logger.error("Erreur dans applyTopSafety", e);
        }
    }
}