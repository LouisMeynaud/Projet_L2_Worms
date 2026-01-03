package com.lllmtb.physics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {
    @Test
    void testConstructor() {
        Vector v1 = new Vector();
        Vector v2 = new Vector(1,1);
        assertEquals(0,v1.getX());
        assertEquals(0,v1.getY());
        assertEquals(1,v2.getX());
        assertEquals(1,v2.getY());
    }
    @Test
    void testSubAddScale() {
        Vector v1 = new Vector(3,4);
        Vector v2 = new Vector(1,1);
        Vector v3 = new Vector(5,6);
        Vector v4 = new Vector(1,2);

        v4 = v4.scale(2);
        v1 = v1.add(v2);
        v3 = v3.sub(v2);
        assertEquals(4,v1.getX());
        assertEquals(5,v1.getY());
        assertEquals(4,v3.getX());
        assertEquals(5,v3.getY());
        assertEquals(2,v4.getX());
        assertEquals(4,v4.getY());
    }
    @Test
    void testEquals() {
        Vector v  = new Vector(-1,1);
        Vector v1 = new Vector(-1,1);
        Vector v2 = new Vector(-1,0);
        Vector v3 = new Vector(Double.MIN_VALUE,1);
        Vector v4 = new Vector(2,Double.POSITIVE_INFINITY);

        assertFalse(v.equals(null));
        assertTrue(v.equals(v1));  // true && true
        assertFalse(v.equals(v2)); // true && false
        assertFalse(v.equals(v3)); // false && true
        assertFalse(v.equals(v4)); // false && false
    }
    @Test
    void testNewPos() {
        Vector v1 = new Vector(3,4);
        Position pos = new Position(1,1);
        Position pos2 = v1.newPos(pos,1);
        assertEquals(4,pos2.getX());
        assertEquals(5,pos2.getY());
    }
    @Test
    void test4() {
        Vector v1 = new Vector(3,4);
        assertEquals("Vect(3.0,4.0)",v1.toString());
    }
    @Test
    void testCopy() {
        Vector v1 = new Vector(3, 4);
        Vector v2 = v1.copy();
        assertEquals(v1.getX(), v2.getX());
        assertEquals(v1.getY(), v2.getY());
        v2 = v2.add(new Vector(1, 1));
        assertEquals(3, v1.getX());
        assertEquals(4, v1.getY());
    }
}
