package com.lllmtb.physics;

import java.util.ArrayList;
import com.lllmtb.terrain.map.*;
import com.lllmtb.model.entity.worm.*;
import com.lllmtb.model.entity.projectile.*;

public class Collision {
    private static ArrayList<PhysicsBody> bodies;
    private static Map map;

    public static void init(ArrayList<PhysicsBody> bodies, Map map) {
        Collision.bodies = bodies;
        Collision.map = map;
    }

    public static Boolean haveCollisionMap(PhysicsBody body, Position nextPos) {
        Position nextPosXBasGauche = new Position(nextPos.getX(), body.getPos().getY());
        Position nextPosYBasGauche = new Position(body.getPos().getX(), nextPos.getY());
        boolean collisionDetected = false;
        if (!map.inMap(nextPos)) {
            bodies.remove(body);

        } else {// distinction de cas pour savoir si on anulle x , y ou les deux.
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
                    Vector tempo = new Vector(0, 0); // important , vue que cubique , le cas ou pas de colision quand on
                                                     // bouge de x , pas de colision quand on bouge de y mais colision
                                                     // quand on bouge de X et Y possible
                    body.setVector(tempo);
                    collisionDetected = true;
                }
            }

            else if (body.getEntity() instanceof Projectile) { // projectile sera ajouter
                if(haveCollisionXY(body,nextPos)){
                bodies.remove(body);
                collisionDetected = true;
            }}
        } // on traiteras ensuite ici les cas ou c'est des projectile etc
        return collisionDetected;
    }

    public static boolean haveCollisionX(PhysicsBody body, Position nextPosXBasGauche) {
        Position nextPosXBasDroite = PosBasDroite(nextPosXBasGauche, body);
        Position nextPosXHautGauche = PosHautGauche(nextPosXBasGauche, body);
        Position nextPosXHautDroite = PosHautDroite(nextPosXBasGauche, body);
        if (body.getVector().getX() < 0) {
            return (map.inMap(nextPosXBasGauche) && map.isSolide(nextPosXBasGauche))
                    || (map.inMap(nextPosXHautGauche) && map.isSolide(nextPosXHautGauche)); // colision vers la gauche rendre ceci plus generique a la suite (dirt)

        } else {
            return (map.inMap(nextPosXBasDroite) && map.isSolide(nextPosXBasDroite))
                    || (map.inMap(nextPosXHautDroite) && map.isSolide(nextPosXHautDroite)); // colision vers la droite rendre ceci plus generique a la suite (dirt)

        }

    }

    public static boolean haveCollisionY(PhysicsBody body, Position nextPosYBasGauche) {
        Position nextPosYBasDroite = PosBasDroite(nextPosYBasGauche, body);
        Position nextPosYHautGauche = PosHautGauche(nextPosYBasGauche, body);
        Position nextPosYHautDroite = PosHautDroite(nextPosYBasGauche, body);
        if (body.getVector().getY() < 0) {
            return (map.inMap(nextPosYBasDroite) && map.isSolide(nextPosYBasDroite))
                    || (map.inMap(nextPosYBasGauche) && map.isSolide(nextPosYBasGauche)); // colision vers le bas rendre ceci plus generique a la suite (dirt)

        } else {
            return (map.inMap(nextPosYHautGauche) && map.isSolide(nextPosYHautGauche))
                    || (map.inMap(nextPosYHautDroite) && map.isSolide(nextPosYHautDroite)); // colision vers le haut rendre ceci plus generique a la suite (dirt)

        }

    }

    public static boolean haveCollisionXY(PhysicsBody body, Position nextPosXYBasGauche) {
        Position nextPosXYBasDroite = PosBasDroite(nextPosXYBasGauche, body);
        Position nextPosXYHautGauche = PosHautGauche(nextPosXYBasGauche, body);
        Position nextPosXYHautDroite = PosHautDroite(nextPosXYBasGauche, body);

        if (body.getVector().getX() < 0 && body.getVector().getY() < 0) {
            return (map.inMap(nextPosXYBasGauche) && map.isSolide(nextPosXYBasGauche)); // colision bas/gauche
        } else if (body.getVector().getX() < 0 && body.getVector().getY() > 0) {
            return (map.inMap(nextPosXYHautGauche) && map.isSolide(nextPosXYHautGauche)); // colision bas/droite
        } else if (body.getVector().getX() > 0 && body.getVector().getY() < 0) {
            return (map.inMap(nextPosXYBasDroite) && map.isSolide(nextPosXYBasDroite)); // colision haut/gauche
        } else if (body.getVector().getX() > 0 && body.getVector().getY() > 0) {
            return (map.inMap(nextPosXYHautDroite) && map.isSolide(nextPosXYHautDroite)); // colision haut/droite
        }
        return false;
    }

    public static Position PosBasDroite(Position Posactuel, PhysicsBody body) {
        return (new Position(Posactuel.getX() + body.getWidth(), Posactuel.getY()));
    }

    public static Position PosHautDroite(Position Posactuel, PhysicsBody body) {
        return (new Position(Posactuel.getX() + body.getWidth(), Posactuel.getY() + body.getHeight())); // on fait moin car dans la matrice les coordonée sont inversée

    }

    public static Position PosHautGauche(Position Posactuel, PhysicsBody body) {
        return (new Position(Posactuel.getX(), Posactuel.getY() + body.getHeight()));
    }
}
