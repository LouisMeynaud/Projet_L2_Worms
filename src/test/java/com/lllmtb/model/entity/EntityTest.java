package com.lllmtb.model.entity;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;



public class EntityTest {

    public static Worm worm;
    public static PhysicsBody pB;

    @BeforeAll
    public static void setUp(){
        int x1 = 10;
        int y1 = 10;
        int x2 = 20;
        int y2 = 20;
        double mass = 100;
        int height = 10;
        int width = 20;
        Position pos1 = new Position(x1, y1);
        Vector vect1 = new Vector(x2, y2);
        pB = new PhysicsBody(pos1,vect1, mass,height,width);
        worm = new Worm(pB);
    }

    @Test
    public void TestConstructorBody(){
        assertEquals(pB, worm.getBody());
    }

    @Test
    public void TestConstructorUuid(){
        assertEquals(1, worm.uuid);
    }
    
}
