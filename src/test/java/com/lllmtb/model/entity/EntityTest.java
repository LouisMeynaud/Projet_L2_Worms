package com.lllmtb.model.entity;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.model.entity.worm.Worm;



public class EntityTest {

    public static Worm worm;
    public static PhysicsBody pB;

    @BeforeAll
    public static void setUp(){
        int x1 = 10;
        int y1 = 10;
        int x2 = 20;
        int y2 = 20;
        Position pos1 = new Position(x1, y1);
        Position pos2 = new Position(x2, y2);
        pB = new PhysicsBody(pos1,pos2);
        worm = new Worm(pB);
    }

    @Test
    public void TestConstructorBody(){
        assertEquals(worm.getBody(), pB);
    }

    @Test
    public void TestConstructorUuid(){
        assertEquals(1, worm.uuid);
    }
    
}
