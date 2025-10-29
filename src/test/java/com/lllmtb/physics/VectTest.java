package com.lllmtb.physics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VectTest {
    @Test
    void test1() {
        Vect v1 = new Vect();
        Vect v2 = new Vect(1,1);
        assertEquals(0,v1.getX());
        assertEquals(0,v1.getY());
        assertEquals(1,v2.getX());
        assertEquals(1,v2.getY());
    }
    @Test
    void test2() {
        Vect v1 = new Vect(3,4);
        Vect v2 = new Vect(1,1);
        Vect v3 = new Vect(5,6);
        Vect v4 = new Vect(1,2);
        v4.scale(2);
        v1.add(v2);
        v3.sub(v2);
        assertEquals(4,v1.getX());
        assertEquals(5,v1.getY());
        assertEquals(4,v3.getX());
        assertEquals(5,v3.getY());
        assertEquals(2,v4.getX());
        assertEquals(4,v4.getY());
        assertTrue(v1.same(v3));
        assertFalse(v1.same(v2));
    }
    @Test
    void test3() {
        Vect v1 = new Vect(3,4);
        Position pos = new Position(1,1);
        Position pos2 = v1.newPos(pos,1);
        assertEquals(4,pos2.getX());
        assertEquals(5,pos2.getY());
    }
    @Test
    void test4() {
        Vect v1 = new Vect(3,4);
        assertEquals("Vect(3.0,4.0)",v1.toString());
    }
}
