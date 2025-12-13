package com.lllmtb.physics;

import com.lllmtb.model.entity.projectile.RocketProjectile;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.Tile;

public class Explosion {

    public void explosionMapEffect(Position position, RocketProjectile projectile, Map map) {

        for (int i = -projectile.getRayon(); i <= projectile.getRayon(); i++) {
            for (int j = -projectile.getRayon(); j <= projectile.getRayon(); j++) {
                double distance = Math.sqrt(i * i + j * j);
                if (distance <= projectile.getRayon()) {
                    Position tilePos = new Position(position.getX() + i, position.getY() + j);
                    if (map.inMap(tilePos) && (!map.isTileAt(tilePos, Tile.WATER))) {
                        map.setTile(tilePos, Tile.AIR);
                    }
                }
            }
        }
    }
}
