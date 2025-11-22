package com.lllmtb.terrain.generation;


import java.io.ObjectInputFilter;
import java.util.Arrays;
import java.util.Random;

import com.lllmtb.physics.Position;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.SaveMap;


public class GenerateMap {
    public static ConfigMap WORLD = new ConfigMap( 100, 4, 5, false, 17, LandScape.HILLY,1,4);
    public static ConfigMap FLYINGWORLD = new ConfigMap( 100, 4, 5, true, 17, LandScape.HILLY,1,4);
    

    /**
     * method for generating a random map
     * 
     * @param config takes a config type object in order to generate a map according to presets
     * @return the create map
     */
    public static Map createMap(ConfigMap config) {
        Map map = new Map(config.getHeight(), config.getWidth(), SaveMap.newMapSaveFile());
        int[] lowLine;

        int[] hightLine = LineGenerator.createLine(config); 
        if (config.isFlyingIsland()) {
            lowLine = LineGenerator.createLine(config); 
            TriHightLineLowLine(hightLine,lowLine,config);
            
        } else {
            lowLine = new int[config.getWidth()];
            for (int i = 0; i < config.getWidth(); i++) {
                lowLine[i] = 0;
            }
        }


        MapBalancer.leftRightBalancing(lowLine, hightLine, config.getHeight());
        MapBalancer.thicknessBalencer(config.getThicknessMin(),MapBalancer.averageThickness(lowLine, hightLine, 0, config.getWidth()), hightLine);
        MapFiller.fillingWater(map, config.getWaterHeight());
        MapFiller.fillingDirt(map, hightLine, lowLine);
        map.setSpawnPoint(createSpawnPoint(config,hightLine));

        map.setHightLine(hightLine);

        return map;
    }
    
     /**
     * methode pour que la hightLine soit toujours au dessus de la lowLine
     * 
     * @param highLine la ligne haute
     * @param lowLine la ligne basse
     * @param config la config de la map
     */
    protected  static void TriHightLineLowLine(int[] hightLine,int[] lowLine,ConfigMap config){
        for (int i = 0; i < config.getWidth(); i++) {
                if (lowLine[i] > hightLine[i]) {
                    int temp = lowLine[i];
                    lowLine[i] = hightLine[i];
                    hightLine[i] = temp;
                }
            }
    }
    // attention probleme, le right balence crée une pente enorme au mileu

    protected  static Position[][] createSpawnPoint(ConfigMap config,int[] highLine){
        int getNbSpawnPoint = config.getNbTeam()*config.getNbWarms();
        Position[] spawnPointPos = new Position[getNbSpawnPoint];
        int i = 0;
        boolean exist;
        Random random = new Random();
        while(i < getNbSpawnPoint){
            exist = false;
            int xPosition = random.nextInt(highLine.length);
            Position nouvellePos = new Position(xPosition, highLine[xPosition]+10);
            for(int j = 0; j< i;j++){
                if(nouvellePos.equals(spawnPointPos[j])){
                    exist = true;
                }
            }
            if(!exist && (nouvellePos.getY() > config.getWaterHeight())){
                spawnPointPos[i] = nouvellePos;
                i++;
            }
            
        }
        return separateSpawnTeam(config,spawnPointPos);}
    
    protected  static Position[][] separateSpawnTeam(ConfigMap config,Position[] spawnPointPos){
        Position[][] teamSpawnPointPositions = new Position[config.getNbTeam()][config.getNbWarms()];
        for (int i = 0; i < config.getNbTeam(); i++) {
            System.out.println("");
            for (int j = 0; j < config.getNbWarms(); j++) {
                teamSpawnPointPositions[i][j] = spawnPointPos[i*config.getNbWarms()+j];
                System.out.println("nouvelle position: x ="+teamSpawnPointPositions[i][j].getX() + "y =" + teamSpawnPointPositions[i][j].getY());
            }
        }
        return teamSpawnPointPositions;
    }
    
    }