package com.lllmtb.physics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class PositionTest {
    @Test
    void testConstructor(){
        int x = 10;
        int y = 10;
        Position pos = new Position(x, y);
        assertEquals(x,pos.getX());
        assertEquals(y,pos.getY());
    }

    @Test
    void testToString(){
        double x = 5;
        double y = 5;
        Position pos = new Position(x, y);
        assertEquals("[" + x + ", " + y + "]", pos.toString());
    }

    @Test
    void testpPosEquals(){
        Position pos = new Position(1, 1);
        Position pos2 = new Position(1, 1);
        assertTrue(pos.equals(pos2));
    }
    @Test
    void testpPosXEqualsYNotEquals(){
        Position pos = new Position(1, 1);
        Position pos2 = new Position(1, 2);
        assertFalse(pos.equals(pos2));
    }
    @Test
    void testpPosYEqualsXNotEquals(){
        Position pos = new Position(1, 1);
        Position pos2 = new Position(2, 1);
        assertFalse(pos.equals(pos2));
    }
    @Test
    void testpPosNotEquals(){
        Position pos = new Position(1, 3);
        Position pos2 = new Position(2, 4);
        assertFalse(pos.equals(pos2));
    }

   

}
