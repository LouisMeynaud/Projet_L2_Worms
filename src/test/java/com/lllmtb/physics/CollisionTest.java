package com.lllmtb.physics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import com.lllmtb.model.entity.*;
import com.lllmtb.physics.*;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.terrain.generation.*;
import com.lllmtb.terrain.map.*;
import com.sun.source.util.SourcePositions;

public class CollisionTest {

    @Test
    void nextPositionBasDroiteTest() {
        Position position = new Position(100, 100);
        Vector vector = new Vector(10, 10);
        PhysicsBody body = new PhysicsBody(position, vector, 20.0, 50, 50);
        Position nouvelle = Collision.nextPositionBasDroite(position, body);
        assertEquals(nouvelle.getX(), 150);
        assertEquals(nouvelle.getY(), 100);
    }

    @Test
    void nextPositionHautDroiteTest() {
        Position position = new Position(100, 100);
        Vector vector = new Vector(10, 10);
        PhysicsBody body = new PhysicsBody(position, vector, 20.0, 50, 50);
        Position nouvelle = Collision.nextPositionHautDroite(position, body);
        assertEquals(nouvelle.getX(), 150);
        assertEquals(nouvelle.getY(), 50); // rappelle : on fait - car les y sont inverser dans la matrice
    }

    @Test
    void nextPositionHautGaucheTest() {
        Position position = new Position(100, 100);
        Vector vector = new Vector(10, 10);
        PhysicsBody body = new PhysicsBody(position, vector, 20.0, 50, 50);
        Position nouvelle = Collision.nextPositionHautGauche(position, body);
        assertEquals(nouvelle.getX(), 100);
        assertEquals(nouvelle.getY(), 50); // rappelle : on fait - car les y sont inverser dans la matrice
    }

    @Test
    void haveCollisionXTestVectXposColision() {
        Map map = MapLoader.loadMatrix("MapTestColision");
        Position position = new Position(9, 8); // on a bien que 9 plus 3 = 12 , et il y a un bloc a la coordonée 12,8
        Vector vector = new Vector(2, 2);
        PhysicsBody body = new PhysicsBody(position, vector, 20.0, 3, 3);
        ArrayList<PhysicsBody> bodies = new ArrayList<>();
        Collision.init(bodies, map);
        assertTrue(Collision.haveCollisionX(body, position));

    }

    // @Test
    // void haveCollisionXTestVectXposnoColision() {
    //     Map map = MapLoader.loadMatrix("MapTestColision");
    //     Position position = new Position(9, 0); // on a bien que 9 plus 3 = 12 , et il y a un pas de bloc a la coordonée 12,0
    //     Vector vector = new Vector(2, 2);
    //     PhysicsBody body = new PhysicsBody(position, vector, 20.0, 3, 3);
    //     ArrayList<PhysicsBody> bodies = new ArrayList<>();
    //     Collision.init(bodies, map);
    //     assertFalse(Collision.haveCollisionX(body, position));
    // }

    // @Test
    // void haveCollisionXTestVectXNegColision(){
    // Map map = MapLoader.loadMatrix("MapTestColision");
    // Position position = new Position(7, 1);
    // Vector vector = new Vector(1,1);
    // PhysicsBody body = new PhysicsBody(position, vector, 20.0,50,50);
    // Position nouvelle = Collision.nextPositionHautGauche(position, body);

    // }

    // @Test
    // void haveCollisionXTestVectXNegnoColision(){
    // Map map = MapLoader.loadMatrix("MapTestColision");
    // Position position = new Position(7, 1);
    // Vector vector = new Vector(1,1);
    // PhysicsBody body = new PhysicsBody(position, vector, 20.0,50,50);
    // Position nouvelle = Collision.nextPositionHautGauche(position, body);

    // }

    // @Test
    // void haveCollisionYTest(){

    // }
    // @Test
    // void haveCollisionXYTest(){

    // }

}
