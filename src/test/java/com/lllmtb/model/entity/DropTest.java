package com.lllmtb.model.entity;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RemoveEntityCommand;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.physics.GroundCheckCollision;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.GameMode;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.generation.LandScape;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DropTest {
    private Drop drop;
    private EventBus eb;
    private GameWorld world;

    @BeforeEach
    public void setUp(){
        this.drop = new Drop(new Position(1,1), WeaponId.GUN);
        GameSettings settings = new GameSettings("blabla", 2, new String[]{"TeamTest1", "TeamTest2"}, 1, 100, 10, false, null, false, 370, LandScape.HILLY, 2,GameMode.NORMAL, true, false);
        this.eb = new EventBus();
        this.world = new GameWorld(settings, eb);
    }

    @AfterEach
    public void endSetUp(){
        world.stop();
    }

    @Test
    public void testConstrutor(){
        assertSame(WeaponId.GUN, drop.itemIdToUnlock);
    }

    @Test
    public void testAddModifierWithoutTouchTheGroud(){
        List<GameCommand> commands =  drop.addModifier();
        assertEquals(2 ,commands.size());
    }

    @Test
    public void testAddModifierTouchProjectile(){
        Projectile proj = Mockito.mock(Projectile.class);
        GameCommand command = drop.onImpact(proj);
        assertInstanceOf(RemoveEntityCommand.class, command);
    }

    @Test
    public void testDropCollisionEventTouchWormWeaponItem(){
        world.start();
        Team team = new Team("Test", 1, new Position[]{new Position(1,1)}, 0);
        Drop drop = new Drop(new Position(1, 1), WeaponId.SHOTGUN);
        drop.onImpact(team.getCurrentWorm());
        try{
            TimeUnit.MILLISECONDS.sleep(10);
        } catch(InterruptedException ignored){}
        assertTrue(team.getInventory().isUnlocked((WeaponId) drop.itemIdToUnlock));
        assertNotEquals(0, team.getInventory().weapons.getAmmo((WeaponId) drop.itemIdToUnlock));
    }

    @Test
    public void testDropCollisionEventTouchWormToolItem(){
        world.start();
        Team team = new Team("Test", 1, new Position[]{new Position(1,1)}, 0);
        Drop drop = new Drop(new Position(1, 1), ToolId.JETPACK);
        drop.onImpact(team.getCurrentWorm());
        try{
            TimeUnit.MILLISECONDS.sleep(10);
        } catch(InterruptedException ignored){}
        assertNotEquals(0, team.getInventory().tools.getAmmo((ToolId) drop.itemIdToUnlock));
    }
}
