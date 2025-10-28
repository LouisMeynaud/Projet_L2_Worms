package com.lllmtb.model.entity;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.model.entity.worm.Worm;



public class EntityTest {

    @Test
    public void TestConstructor(){
        int x1 = 10;
        int y1 = 10;
        int x2 = 20;
        int y2 = 20;
        Position pos1 = new Position(x1, y1);
        Position pos2 = new Position(x2, y2);
        PhysicsBody pB = new PhysicsBody(pos1,pos2);
        Worm worm = new Worm(pB);
        assertEquals(1, worm.uuid);
        assertEquals(pB, worm.getBody());
    }
    
}
