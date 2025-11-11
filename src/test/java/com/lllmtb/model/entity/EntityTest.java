package com.lllmtb.model.entity;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.Position;



public class EntityTest {

    public static Worm worm;

    @BeforeAll
    public static void setUp(){
        int x1 = 10;
        int y1 = 10;
        Position pos1 = new Position(x1, y1);
        worm = new Worm(pos1);
    }

    @Test
    public void testGetBodyNotNull(){
        assertNotEquals(null , worm.getBody());
    }

    @Test
    public void testGetBody(){
        assertEquals(worm, worm.getBody().getEntity());
    }


    @Test
    public void testConstructorUuid(){
        assertNotEquals(0, worm.uuid);
    }
    
}
