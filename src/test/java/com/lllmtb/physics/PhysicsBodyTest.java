package com.lllmtb.physics;

import org.junit.jupiter.api.Test;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.worm.Worm;

import static org.junit.jupiter.api.Assertions.*;

public class PhysicsBodyTest {
@Test
void testConstructor(){
    int x1 = 10;
    int y1 = 10;
    int x2 = 20;
    int y2 = 20;
    double mass1 = 0.5;
    int height1 = 10;
    int width1 = 10;

    Position pos1 = new Position(x1, y1);
    Entity entity = new Worm(pos1);
    Vector vector1 = new Vector(x2, y2);
    PhysicsBody pB = new PhysicsBody(entity, pos1,vector1, mass1, height1,width1);
    assertEquals(entity, pB.getEntity());
    assertEquals(x1,pB.getPos().getX());
    assertEquals(y1,pB.getPos().getX());
    assertEquals(x2,pB.getVector().getX());
    assertEquals(y2,pB.getVector().getY());
    assertEquals(mass1,pB.getMass());
    assertEquals(height1,pB.getHeight());
    assertEquals(width1,pB.getWidth());
}
@Test
void testSet(){
    int x1 = 10;
    int y1 = 10;
    int x2 = 20;
    int y2 = 20;
    int x3 = 30;
    int y3 = 30;
    int x4 = 40;
    int y4 = 40;
    double mass1 = 0.5;
    int height1 = 10;
    int width1 = 10;

    Position pos1 = new Position(x1, y1);
    Entity entity = new Worm(pos1);
    Vector vector2 = new Vector(x2, y2);
    Position pos3 = new Position(x3, y3);
    Vector vector4 = new Vector(x4, y4);
    PhysicsBody pB = new PhysicsBody(entity,pos1,vector2,mass1,height1,width1);
    pB.setPos(pos3);
    pB.setVector(vector4);
    assertEquals(x3,pB.getPos().getX());
    assertEquals(y3,pB.getPos().getX());
    assertEquals(x4,pB.getVector().getX());
    assertEquals(y4,pB.getVector().getY());

}
}
