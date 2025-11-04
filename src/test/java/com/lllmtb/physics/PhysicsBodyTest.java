package com.lllmtb.physics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PhysicsBodyTest {
@Test
void testConstructor(){
    int x1 = 10;
    int y1 = 10;
    int x2 = 20;
    int y2 = 20;
    Position pos1 = new Position(x1, y1);
    Position pos2 = new Position(x2, y2);
    PhysicsBody pB = new PhysicsBody(pos1,pos2);
    assertEquals(x1,pB.getPos().getX());
    assertEquals(y1,pB.getPos().getX());
    assertEquals(x2,pB.getVect().getX());
    assertEquals(y2,pB.getVect().getY());

}
}
