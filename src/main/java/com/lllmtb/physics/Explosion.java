package com.lllmtb.physics;

import java.util.concurrent.CopyOnWriteArrayList;

import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.Tile;

public class Explosion {

    /**
     * create whole around the explosion (replace tile by AIR)
     * @param position the position of center of explosion
     * @param rayon    the rayon of the explosion
     * @param map      the map
     */
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

        /**
         * projects entities that are within the explosion radius
         * @param position  the position of center of explosion
         * @param suffleRadius  the radius of the wind created by the explosion
         * @param explosionForce the force of the wind
         * @param map  the map
         * @param Bodies the list of bodies in the game
         */
        public static void explosionSuffleEffect(Position position, int suffleRadius, double explosionForce, Map map, CopyOnWriteArrayList<PhysicsBody> bodies) {
        for (PhysicsBody body : bodies) {
            Position bodyPos = body.getPos();

            double dx = bodyPos.getX() - position.getX();
            double dy = bodyPos.getY() - position.getY();
            double distance = Math.sqrt(dx * dx + dy * dy); // Distance to explosion center

            if (distance <= suffleRadius) {
                // Direction
                Vector direction;
                if (distance < 0.1) {
                    direction = new Vector(0, 1);
                } else {
                    direction = new Vector(dx / distance, dy / distance);
                }
                double ratio = 1 - (distance / suffleRadius);
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

    /**
     *
     * @param position the position of the center of the explosion
     * @param damageRadius the radius within which the entity takes damage
     * @param maxDamage the maximun damage of the explosion (at the center)
     * @param Bodies the list of bodies
     */
    public static void explosionDamageEffect(Position position, int damageRadius, int maxDamage, CopyOnWriteArrayList<PhysicsBody> bodies) {
    for (PhysicsBody body : bodies) {
        Position bodyPos = body.getPos();
        double dx = bodyPos.getX() - position.getX();
        double dy = bodyPos.getY() - position.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= damageRadius) {
            double ratio = 1 - (distance / damageRadius);
            int actualDamage = (int) (maxDamage * ratio);
            if (body.getEntity() instanceof Worm) {
                ((Worm) body.getEntity()).damageWorm(actualDamage);
            }
        }
    }
}
}
