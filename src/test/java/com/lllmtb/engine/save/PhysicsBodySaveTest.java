package com.lllmtb.engine.save;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PhysicsBodySaveTest {
    @Test
    public void constructorTest() {
        Position position = new Position(3,2);
        Team team = new Team("test",1, new Position[]{position},150);
        Worm worm = new Worm("Test", team);
        Vector vector= new Vector(1,2);
        PhysicsBody physicsBody = new PhysicsBody(worm,position, vector,3.4,2,1);
        PhysicsBodySave save = new PhysicsBodySave(physicsBody);
        assertNotNull(save);
        assertEquals(worm, save.entity);
        assertEquals(worm.uuid, save.uuid);
    }
}
