package com.lllmtb.terrain.map;

import java.util.Random;

import com.lllmtb.physics.Position;

public class CreateSpawnPoint {

    /**
     * Creates spawn points for teams on the map.
     * @param nbTeams Number of teams.
     * @param nbWorms Number of worms per team.
     * @param map The game map.
     * @return A 2D array of Positions representing spawn points for each team and worm. (one little array per team)
     */
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
        int totalGlobalAttempts = 0;

        while (i < nbSpawnPoint) {
            attemptsForCurrentWorm++;
            totalGlobalAttempts++;

            if (attemptsForCurrentWorm % 10 == 0 && minDistance > 0) {
                minDistance--;
            }

            if (totalGlobalAttempts > 1000) {
                spawnPointPos[i] = new Position(random.nextInt(mapWidth), mapHeight - 10);
                i++;
                attemptsForCurrentWorm = 0;
                continue;
            }

            int x = random.nextInt(mapWidth);
            boolean wormPlaced = false;

            for (int y = mapHeight - 1; y > waterLevel; y--) {
                Position headPos = new Position(x, y);

                boolean headIsgood = !map.isSolid(new Position(x, y)) && map.getTile(new Position(x, y)).getDamage() == 0;
                boolean bodyIsGood = !map.isSolid(new Position(x, y - 1)) && map.getTile(new Position(x, y)).getDamage() == 0;
                boolean feetIsGood = map.isSolid(new Position(x, y - 2)) && map.getTile(new Position(x, y)).getDamage() == 0;

                if (headIsgood && bodyIsGood && feetIsGood) {
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
            for (int j = 0; j < nbWorms; j++) {
                teamSpawnPointPositions[i][j] = spawnPointPos[i * nbWorms + j];
            }
        }
        return teamSpawnPointPositions;
    }
}
