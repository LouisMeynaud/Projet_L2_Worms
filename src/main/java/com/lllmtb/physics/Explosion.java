package com.lllmtb.physics;

import java.util.concurrent.CopyOnWriteArrayList;

import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.Tile;

public class Explosion {

    
    public static void explosionMapEffect(Position position, int rayon, Map map) {

        for (int i = -rayon; i <= rayon; i++) {
            for (int j = -rayon; j <= rayon; j++) {
                if ((i * i + j * j) <= (rayon * rayon)) {
                    Position tilePos = new Position(position.getX() + i, position.getY() + j);
                    if (map.inMap(tilePos) && (!map.isTileAt(tilePos, Tile.WATER))) {
                        map.setTile(tilePos, Tile.AIR);
                    }
                }
            }
        }
    }

        
        public static void explosionSuffleEffect(Position position, int SuffleRadius, double explosionForce, Map map, CopyOnWriteArrayList<PhysicsBody> Bodies) {
        for (PhysicsBody body : Bodies) {
            Position bodyPos = body.getPos();
            
            double dx = bodyPos.getX() - position.getX();
            double dy = bodyPos.getY() - position.getY();
            double distance = Math.sqrt(dx * dx + dy * dy); // Distance to explosion center

            if (distance <= SuffleRadius) {
                // Direction
                Vector direction;
                if (distance < 0.1) {
                    direction = new Vector(0, 1);
                } else {
                    direction = new Vector(dx / distance, dy / distance);
                }

                double ratio = 1 - (distance / SuffleRadius);
                
                double suffleForce = explosionForce * ratio; 
                
                Vector pushForce = direction.scale(suffleForce); 
                pushForce = pushForce.add(new Vector(0, 10.0));
                body.setVector(body.getVector().add(pushForce));

                if (body.getEntity() instanceof Worm) {
                    ((Worm) body.getEntity()).setOnGround(false);
                }
            }
        }
    }
}
