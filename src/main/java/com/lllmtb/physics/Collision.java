package com.lllmtb.physics;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.lllmtb.terrain.map.*;
import com.lllmtb.model.entity.worm.*;
import com.lllmtb.model.entity.projectile.*;

public class Collision {

    
    private static CopyOnWriteArrayList<PhysicsBody> bodies;
    private static Map map;

    /**
     * 
     * @param bodies the table containing all PhysiqueBodie
     * @param map take the map
     */
    public static void init(CopyOnWriteArrayList<PhysicsBody> bodies, Map map) {
        Collision.bodies = bodies;
        Collision.map = map;
    }

    /**
     * 
     * @param body the body where collisions will be tested
     * @param nextPos the next position of the entity, the one on which we will test whether a collision is detected in order to prevent it from happening or not.
     * @return boolean, allowing effects to be applied subsequently.
     */

    public static Boolean haveCollisionMap(PhysicsBody body, Position nextPos) {
        Position nextPosXBasGauche = new Position(nextPos.getX(), body.getPos().getY());
        Position nextPosYBasGauche = new Position(body.getPos().getX(), nextPos.getY());
        boolean collisionDetected = false;
        if (!map.inMap(nextPos)) {
            bodies.remove(body);
            return true;

        } else {// case distinction to know whether to cancel x, y or both.
            if (body.getEntity() instanceof Worm) {
                if (haveCollisionX(body, nextPosXBasGauche)) {
                    Vector tempo = new Vector(0, body.getVector().getY());
                    body.setVector(tempo);
                    collisionDetected = true;

                } else if (haveCollisionY(body, nextPosYBasGauche)) {
                    Vector tempo = new Vector(body.getVector().getX(), 0);
                    body.setVector(tempo);
                    collisionDetected = true;
                }

                else if (haveCollisionXY(body, nextPos)) {
                    Vector tempo = new Vector(0, 0); // Important, given that it's a cubic structure, the case where there's no collision when moving along the x-axis, no collision when moving along the y-axis, but collision is possible when moving along both the x and y axes.
                    body.setVector(tempo);
                    collisionDetected = true;
                }
            }

            else if (body.getEntity() instanceof Projectile) { // projectile sera ajouter
                if(haveCollisionXY(body,nextPos)){
                bodies.remove(body);
                collisionDetected = true;
            }}
        } // on traiteras ensuite ici les cas ou c'est des grenade  etc
        return collisionDetected;
    }

    /**
     * 
     * @param body the body where collisions will be tested
     * @param nextPosXBasGauche the bottom left corner of the entity
     * @return returns if there is a collision on the x-axis
     */
    public static boolean haveCollisionX(PhysicsBody body, Position nextPosXBasGauche) {
        Position nextPosXBasDroite = PosBasDroite(nextPosXBasGauche, body);
        Position nextPosXHautGauche = PosHautGauche(nextPosXBasGauche, body);
        Position nextPosXHautDroite = PosHautDroite(nextPosXBasGauche, body);
        if (body.getVector().getX() < 0) {
            return (map.inMap(nextPosXBasGauche) && map.isSolid(nextPosXBasGauche))
                    || (map.inMap(nextPosXHautGauche) && map.isSolid(nextPosXHautGauche)); // collision to the left

        } else {
            return (map.inMap(nextPosXBasDroite) && map.isSolid(nextPosXBasDroite))
                    || (map.inMap(nextPosXHautDroite) && map.isSolid(nextPosXHautDroite)); // collision to the right

        }

    }
    /**
     * 
     * @param body the body where collisions will be tested
     * @param nextPosXBasGauche the bottom left corner of the entity
     * @return returns if there is a collision on the Y axis
     */
    public static boolean haveCollisionY(PhysicsBody body, Position nextPosYBasGauche) {
        Position nextPosYBasDroite = PosBasDroite(nextPosYBasGauche, body);
        Position nextPosYHautGauche = PosHautGauche(nextPosYBasGauche, body);
        Position nextPosYHautDroite = PosHautDroite(nextPosYBasGauche, body);
        if (body.getVector().getY() < 0) {
            return (map.inMap(nextPosYBasDroite) && map.isSolid(nextPosYBasDroite))
                    || (map.inMap(nextPosYBasGauche) && map.isSolid(nextPosYBasGauche)); // downward collision
        } else {
            return (map.inMap(nextPosYHautGauche) && map.isSolid(nextPosYHautGauche))
                    || (map.inMap(nextPosYHautDroite) && map.isSolid(nextPosYHautDroite)); // upward collision

        }

    }

     /**
     * 
     * @param body the body where collisions will be tested
     * @param nextPosXBasGauche the bottom left corner of the entity
     * @return returns if there is a collision on the XY axis
     */
    public static boolean haveCollisionXY(PhysicsBody body, Position nextPosXYBasGauche) {
        Position nextPosXYBasDroite = PosBasDroite(nextPosXYBasGauche, body);
        Position nextPosXYHautGauche = PosHautGauche(nextPosXYBasGauche, body);
        Position nextPosXYHautDroite = PosHautDroite(nextPosXYBasGauche, body);

        if (body.getVector().getX() < 0 && body.getVector().getY() < 0) {
            return (map.inMap(nextPosXYBasGauche) && map.isSolid(nextPosXYBasGauche)); // collision bottom/left
        } else if (body.getVector().getX() < 0 && body.getVector().getY() > 0) {
            return (map.inMap(nextPosXYHautGauche) && map.isSolid(nextPosXYHautGauche)); // collision bottom/right
        } else if (body.getVector().getX() > 0 && body.getVector().getY() < 0) {
            return (map.inMap(nextPosXYBasDroite) && map.isSolid(nextPosXYBasDroite)); // collision top/left
        } else if (body.getVector().getX() > 0 && body.getVector().getY() > 0) {
            return (map.inMap(nextPosXYHautDroite) && map.isSolid(nextPosXYHautDroite)); // collision top/right
        }
        return false;
    }

    /**
     * 
     * @param nextPosXBasGauche the position of the dot in the bottom left corner of the warms
     * @param body
     * @return the position of the corner in the bottom right
     */
    public static Position PosBasDroite(Position nextPosXBasGauche, PhysicsBody body) {
        return (new Position(nextPosXBasGauche.getX() + body.getWidth(), nextPosXBasGauche.getY()));
    }
     /**
     * 
     * @param nextPosXBasGauche the position of the dot in the bottom left corner of the warms
     * @param body
     * @return the position of the corner in the bottom right
     */
    public static Position PosHautDroite(Position nextPosXBasGauche, PhysicsBody body) {
        return (new Position(nextPosXBasGauche.getX() + body.getWidth(), nextPosXBasGauche.getY() + body.getHeight()));

    }
     /**
     * 
     * @param nextPosXBasGauche the position of the dot in the bottom left corner of the warms
     * @param body
     * @return the position of the corner in the upper left
     */
    public static Position PosHautGauche(Position nextPosXBasGauche, PhysicsBody body) {
        return (new Position(nextPosXBasGauche.getX(), nextPosXBasGauche.getY() + body.getHeight()));
    }
}
