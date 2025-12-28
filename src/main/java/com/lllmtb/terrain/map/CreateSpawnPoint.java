package com.lllmtb.terrain.map;

import java.util.Random;

import com.lllmtb.physics.Position;

public class CreateSpawnPoint {

    public static Position[][] createSpawnPoint(int nbTeams, int nbWorms, Map map) {
        int nbSpawnPoint = nbTeams * nbWorms;
        Position[] spawnPointPos = new Position[nbSpawnPoint];
        int i = 0;
        Random random = new Random();

        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();
        int waterLevel = map.getHeightWater();
        int minDistance = 5;
        int attemptsForCurrentWorm = 0;

        while (i < nbSpawnPoint) {
            attemptsForCurrentWorm++;

            if (attemptsForCurrentWorm % 10 == 0 && minDistance > 0) {
                minDistance--;
            }

            int x = random.nextInt(mapWidth);
            boolean wormPlaced = false;

            for (int y = mapHeight - 1; y > waterLevel; y--) {
                Position headPos = new Position(x, y);

                boolean headIsFree = !map.isSolid(new Position(x, y));
                boolean feetAreSolid = map.isSolid(new Position(x, y - 2));

                if (headIsFree && feetAreSolid) {
                    boolean occupied = false;
                    for (int j = 0; j < i; j++) {
                        Position existingWorm = spawnPointPos[j];
                        double diffX = Math.abs(headPos.getX() - existingWorm.getX());
                        double diffY = Math.abs(headPos.getY() - existingWorm.getY());
                        if (diffX <= minDistance && diffY <= minDistance) {
                            occupied = true;
                            break;
                        }
                    }

                    if (!occupied && !wormPlaced) {
                        spawnPointPos[i] = headPos;
                        i++;
                        wormPlaced = true;
                        minDistance = 5;
                        attemptsForCurrentWorm = 0;
                    }
                }
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
