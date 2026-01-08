package com.lllmtb.model.entity;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RemoveEntityCommand;
import com.lllmtb.events.EventBus;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.GameMode;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;
import com.lllmtb.terrain.generation.ConfigMap;
import com.lllmtb.terrain.generation.LandScape;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class DropTest {
    private Drop drop;
    private GameWorld world;

    @BeforeEach
    public void setUp(){
        this.drop = new Drop(new Position(1,1), WeaponId.GUN);
        ConfigMap configMap = new ConfigMap(100, 0,0, false,0, LandScape.HILLY, null);
        GameSettings settings = new GameSettings("blabla", 2, new String[]{"TeamTest1", "TeamTest2"}, new boolean[2],1, 100, 10, false, null,configMap, 2,GameMode.NORMAL, true, false);
        EventBus eb = new EventBus();
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
        Team team = new Team("Test", 1, new Position[]{new Position(1,1)}, 0);
        Drop drop = new Drop(new Position(1, 1), WeaponId.SHOTGUN);
        GameCommand cmd = drop.onImpact(team.getCurrentWorm());
        if (cmd != null) cmd.execute(world);
        assertTrue(team.getInventory().isUnlocked((WeaponId) drop.itemIdToUnlock));
        assertNotEquals(0, team.getInventory().weapons.getAmmo((WeaponId) drop.itemIdToUnlock));
    }

    @Test
    public void testDropCollisionEventTouchWormToolItem(){
        Team team = new Team("Test", 1, new Position[]{new Position(1,1)}, 0);
        Drop drop = new Drop(new Position(1, 1), ToolId.JETPACK);

        GameCommand cmd = drop.onImpact(team.getCurrentWorm());
        if(cmd != null) cmd.execute(world);

        assertNotEquals(0, team.getInventory().tools.getAmmo((ToolId) drop.itemIdToUnlock));
    }

    @Test
    public void testDropCollisionEventTouchItem(){
        Team team = new Team("Test", 1, new Position[]{new Position(1,1)}, 0);
        ItemId item = WeaponId.SHOTGUN;
        Drop drop = new Drop(new Position(1, 1), item);
        GameCommand cmd = drop.onImpact(team.getCurrentWorm());
        assertNotNull(cmd);
    }

    @Test
    public void testOnImpactWithIgnoredEntity() {
        Drop drop = new Drop(new Position(1, 1), WeaponId.SHOTGUN);
        Drop otherDrop = new Drop(new Position(2, 2), ToolId.JETPACK);

        GameCommand result = drop.onImpact(otherDrop);

        assertNull(result);
    }

    @Test
    public void testSpeedMaxDrop(){
        world.start();
        Drop drop = new Drop(new Position(1,1), WeaponId.SHOTGUN);
        world.queueCommands(drop.addModifier());
        try{
            TimeUnit.MILLISECONDS.sleep(50);
        } catch(InterruptedException ignored){}
        drop.getBody().setVector(new Vector(0, -15));
        try{
            TimeUnit.MILLISECONDS.sleep(50);
        } catch(InterruptedException ignored){}
        assertTrue(drop.getBody().getVector().getY() <= 9.0);
    }

    @Test
    public void getTest(){
        world.start();
        assertEquals(WeaponId.GUN.name(), drop.getItemName());
        assertEquals("WEAPONID", drop.getItemCategory());
    }


    @Test
    void testIsOnGround_Initial() {

        Drop drop = new Drop(new Position(10, 10), WeaponId.BAZOOKA);
        assertFalse(drop.isOnGround());
    }

    @Test
    void testSetOnGround() {
        Drop drop = new Drop(new Position(10, 10), WeaponId.BAZOOKA);
        drop.setOnGround(true);
        assertTrue(drop.isOnGround());
    }
}
