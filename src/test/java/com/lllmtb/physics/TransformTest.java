package com.lllmtb.physics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransformTest {

    @Test
    public void modifierTest() {
        Vector v = new Vector();
        Transform m = v1 -> new Vector(1,2);
        Vector v2 = m.apply(v);
        assertEquals(1, v2.getX());
        assertEquals(2, v2.getY());
    }
}
