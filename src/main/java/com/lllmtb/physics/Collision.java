package com.lllmtb.physics;
import java.util.ArrayList;
import com.lllmtb.terrain.map.*;

public class Collision {
    private  static ArrayList<PhysicsBody> bodies;
    private static Map map;

    public static void init(ArrayList<PhysicsBody> bodies,Map map){
        Collision.bodies = bodies;
        Collision.map = map;
    }

    public static Boolean haveCollisionMap(PhysicsBody body,Position nextPosition) {
        Position nextPosXBasGauche = map.cooMapToCooMatrix(new Position(nextPosition.getX(),body.getPos().getY()));
        Position nextPosYBasGauche = map.cooMapToCooMatrix(new Position(body.getPos().getX(),nextPosition.getY()));
        boolean collisionDetected = false;
        if (!map.inMap(nextPosition)) {
            bodies.remove(body);
        
        } else {//distinction de cas pour savoir si on anulle x , y ou les deux.
            if (haveCollisionX(body, nextPosXBasGauche)) {
                Vector tempo = new Vector(0, body.getVector().getY());
                body.setVector(tempo);
                collisionDetected = true;

            } else if (haveCollisionY(body, nextPosYBasGauche)) {
                Vector tempo = new Vector(body.getVector().getX(), 0);
                body.setVector(tempo);
                collisionDetected = true;
            }

            else if(haveCollisionXY(body,nextPosition)){
                Vector tempo = new Vector(0, 0); //important , vue que cubique , le cas ou pas de colision quand on bouge de x , pas de colision quand on bouge de y mais colision quand on bouge de X et Y possible
                body.setVector(tempo);
                collisionDetected = true;
            }
        }//on traiteras ensuite ici les cas ou c'est des projectile etc
        return collisionDetected;
    }



public static boolean haveCollisionX(PhysicsBody body,Position nextPosXBasGauche){
    Position nextPosXBasDroite = nextPositionBasDroite(nextPosXBasGauche, body);
    Position nextPosXHautGauche = nextPositionHautGauche(nextPosXBasGauche, body);
    Position nextPosXHautDroite = nextPositionHautDroite(nextPosXBasGauche, body);
    if(body.getVector().getX() < 0){
        return (map.inMap(nextPosXBasGauche)&& map.isTileAt(nextPosXBasGauche, Tile.DIRT))||(map.inMap(nextPosXHautGauche)&& map.isTileAt(nextPosXHautGauche, Tile.DIRT)); //colision vers la gauche //rendre ceci plus generique a la suite (dirt)
    
    }else if(body.getVector().getX()>0){
        return (map.inMap(nextPosXBasDroite)&&map.isTileAt(nextPosXBasDroite, Tile.DIRT))|| (map.inMap(nextPosXHautDroite)&& map.isTileAt(nextPosXHautDroite, Tile.DIRT)); //colision vers la droite //rendre ceci plus generique a la suite (dirt)
    
    }return false;

    }

public static boolean haveCollisionY(PhysicsBody body,Position nextPosYBasGauche){
    Position nextPosYBasDroite = nextPositionBasDroite(nextPosYBasGauche, body);
    Position nextPosYHautGauche = nextPositionHautGauche(nextPosYBasGauche, body);
    Position nextPosYHautDroite = nextPositionHautDroite(nextPosYBasGauche, body);
    if(body.getVector().getY() < 0){
        return (map.inMap(nextPosYBasDroite)&&map.isTileAt(nextPosYBasDroite, Tile.DIRT))|| (map.inMap(nextPosYBasGauche)&&map.isTileAt(nextPosYBasGauche, Tile.DIRT)); //colision vers le bas //rendre ceci plus generique a la suite (dirt)
        
    }else if(body.getVector().getY()>0){
        return (map.inMap(nextPosYHautGauche)&&map.isTileAt(nextPosYHautGauche, Tile.DIRT)) || (map.inMap(nextPosYHautDroite)&&map.isTileAt(nextPosYHautDroite, Tile.DIRT)); //colision vers le haut //rendre ceci plus generique a la suite (dirt)

    }return false;

    }

    public static boolean haveCollisionXY(PhysicsBody body,Position nextPosXYBasGauche){
        Position nextPosXYBasDroite = nextPositionBasDroite(nextPosXYBasGauche, body);
        Position nextPosXYHautGauche = nextPositionHautGauche(nextPosXYBasGauche, body);
        Position nextPosXYHautDroite = nextPositionHautDroite(nextPosXYBasGauche, body);

        if(body.getVector().getX() < 0 && body.getVector().getY()<0){
            return (map.inMap(nextPosXYBasGauche)&& map.isTileAt(nextPosXYBasGauche, Tile.DIRT)); //colision bas/gauche
            }
        else if(body.getVector().getX() < 0 && body.getVector().getY()>0){
            return (map.inMap(nextPosXYHautGauche)&& map.isTileAt(nextPosXYHautGauche, Tile.DIRT)); //colision bas/droite
            }
        else if(body.getVector().getX() > 0 && body.getVector().getY()<0){
            return (map.inMap(nextPosXYBasDroite)&& map.isTileAt(nextPosXYBasDroite, Tile.DIRT)); //colision haut/gauche
            }
        else if(body.getVector().getX() > 0 && body.getVector().getY()>0){
            return (map.inMap(nextPosXYHautDroite)&& map.isTileAt(nextPosXYHautDroite, Tile.DIRT)); //colision haut/droite
            }return false;
}


public static Position nextPositionBasDroite(Position positionactuel,PhysicsBody body){
    return (new Position(positionactuel.getX() + body.getWidth(),positionactuel.getY()));
}
public static Position nextPositionHautDroite(Position positionactuel,PhysicsBody body){
    return (new Position(positionactuel.getX() + body.getWidth(),positionactuel.getY()- body.getHeight())); // on fait moin car dans la matrice les coordonée sont inversée
 
}
public static Position nextPositionHautGauche(Position positionactuel,PhysicsBody body){
    return (new Position(positionactuel.getX(), positionactuel.getY()-body.getHeight()));
}
}
