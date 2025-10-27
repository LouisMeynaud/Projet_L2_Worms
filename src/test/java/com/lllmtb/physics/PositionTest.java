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
}
