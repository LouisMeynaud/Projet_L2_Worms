package com.lllmtb.model.entity;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.DropCollisionEvent;
import com.lllmtb.events.physics.GroundCheckCollision;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.GameMode;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.generation.LandScape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DropTest {
    private Drop drop;

    @BeforeEach
    public void setUp(){
        this.drop = new Drop(new Position(1,1), WeaponId.GUN);
    }

    @Test
    public void testConstrutor(){
        assertSame(WeaponId.GUN, drop.itemIdToUnlock);
    }

    @Test
    public void testAddModifierWithoutTouchTheGroud(){
        List<GameCommand> commands =  drop.addModifier();
        assertEquals(3 ,commands.size());
    }

    @Test
    public void testAddModifierTouchTheGround(){
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"Team"}, 1, 100, 10, false, null, false, 200, LandScape.FLAT,2, GameMode.NORMAL, true);
        EventBus eb = new EventBus();
        GameWorld world = new GameWorld(settings, eb);
        world.start();
        List<GameCommand> commands =  drop.addModifier();
        PhysicsBody ancientPhysicsBody = drop.getBody();
        world.getPhysics().getBodies().add(ancientPhysicsBody);
        world.queueCommands(commands);
        try{
            TimeUnit.MILLISECONDS.sleep(50);
        } catch(InterruptedException ignored){}
        eb.postSync(new GroundCheckCollision(drop));
        assertEquals(CollisionType.CROSS, drop.getCollisionType());
        assertFalse(world.getPhysics().getBodies().contains(ancientPhysicsBody));
    }

    @Test
    public void testDropCollisionEventWeaponItem(){
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"Team"}, 1, 100, 10, false, null, false, 200, LandScape.FLAT,2, GameMode.NORMAL, true);
        EventBus eb = new EventBus();
        GameWorld world = new GameWorld(settings, eb);
        world.start();
        Team team = new Team("Test", 1, new Position[]{new Position(1,1)});
        Drop drop = new Drop(new Position(1, 1), WeaponId.SHOTGUN);
        eb.postSync(new DropCollisionEvent(drop, team));
        try{
            TimeUnit.MILLISECONDS.sleep(10);
        } catch(InterruptedException ignored){}
        assertTrue(team.getInventory().isUnlocked((WeaponId) drop.itemIdToUnlock));
        assertNotEquals(0, team.getInventory().weapons.getAmmo((WeaponId) drop.itemIdToUnlock));
    }

    @Test
    public void testDropCollisionEventToolItem(){
        GameSettings settings = new GameSettings("blabla", 1, new String[]{"Team"}, 1, 100, 10, false, null, false, 200, LandScape.FLAT,2, GameMode.NORMAL, true);
        EventBus eb = new EventBus();
        GameWorld world = new GameWorld(settings, eb);
        world.start();
        Team team = new Team("Test", 1, new Position[]{new Position(1,1)});
        Drop drop = new Drop(new Position(1, 1), ToolId.JETPACK);
        eb.postSync(new DropCollisionEvent(drop, team));
        try{
            TimeUnit.MILLISECONDS.sleep(10);
        } catch(InterruptedException ignored){}
        assertNotEquals(0, team.getInventory().tools.getAmmo((ToolId) drop.itemIdToUnlock));
    }
}
