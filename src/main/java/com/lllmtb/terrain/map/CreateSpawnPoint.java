package com.lllmtb.terrain.map;

import java.util.Random;

import com.lllmtb.physics.Position;

public class CreateSpawnPoint {


    public static Position[][] createSpawnPoint(int nbTeams, int nbWorms, Map map) {
        int nbspawnPoint = nbTeams * nbWorms;
        Position[] spawnPointPos = new Position[nbspawnPoint];
        int i = 0;
        boolean exist;
        Random random = new Random();
        while (i < nbspawnPoint) {
            exist = false;
            int xPosition = random.nextInt(map.getHightLine().length);
            Position nouvellePos = new Position(xPosition, map.getHightLine()[xPosition]);
            for (int j = 0; j < i; j++) {
                if (nouvellePos.equals(spawnPointPos[j])) {
                    exist = true;
                }
            }
            int waterLevel = map.getHeightWater();
            Position sousNouvellePos = new Position(nouvellePos.getX(), nouvellePos.getY() - 1);
            if (!exist && !map.isTileAt(sousNouvellePos, Tile.WATER) && nouvellePos.getY() > waterLevel) {

                spawnPointPos[i] = nouvellePos;
                i++;
            }

        }
        return separateSpawnTeam(nbTeams, nbWorms, spawnPointPos);
    }

    private static Position[][] separateSpawnTeam(int nbTeams, int nbWorms, Position[] spawnPointPos) {
        Position[][] teamSpawnPointPositions = new Position[nbTeams][nbWorms];
        for (int i = 0; i < nbTeams; i++) {
            System.out.println("");
            for (int j = 0; j < nbWorms; j++) {
                teamSpawnPointPositions[i][j] = spawnPointPos[i * nbWorms + j];
            }
        }
        return teamSpawnPointPositions;
    }
}
