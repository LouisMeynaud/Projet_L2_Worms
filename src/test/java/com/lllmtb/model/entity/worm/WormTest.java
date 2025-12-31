package com.lllmtb.model.entity.worm;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.lllmtb.model.entity.effect.BurnEffect;
import com.lllmtb.model.entity.effect.PoisonEffect;
import com.lllmtb.model.entity.effect.StatusEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;

public class WormTest {
    public Worm worm; 
    public Team team;


    @BeforeEach
    public void setUp(){
        String name = "Test";
        Position pos = new Position(0, 0);
        team = new Team("Team", 1, new Position[]{pos});
        worm = new Worm(name, team);
    }


    @Test
    public void testConstructorName(){
        assertEquals("Test", worm.name);
    }

    @Test
    public void testConstructorStats(){
        assertNotEquals(null, worm.getStats());
    }

    @Test
    public void testConstructorTeam(){
        assertEquals(team, worm.getTeam());
    }

    @Test
    public void testIsAliveTrue(){
        assertTrue(worm.isAlive());
    }
    @Test
    public void testIsAliveFalse(){
        worm.damageWorm(worm.getStats().getHp());
        assertFalse(worm.isAlive());
    }
    @Test
    public void testSetDeathFailed(){
        worm.damageWorm(0);
        assertTrue(worm.isAlive());
    }
    @Test
    public void testSpawn(){
        Position pos = new Position(1,1);
        worm.spawn(pos);
        assertEquals(pos, worm.getBody().getPos());
    }

    @Test
    public void testToString() {
        assertDoesNotThrow(() -> worm.toString());
        assertTrue(worm.isAlive());
        assertTrue(worm.toString().contains("♥"));
        worm.damageWorm(worm.getStats().getHp());
        assertFalse(worm.isAlive());
        assertTrue(worm.toString().contains("☠"));
    }

    //TODO : refaire se test
    // @Test
    // public void testGetAnimation() {
        
    //     assertNotNull(worm.getAnimation());
    // }

    @Test
    public void testGetFacingDirection() {
        worm.setFacingDirection(1);
        assertEquals(1, worm.getFacingDirection());

        worm.setFacingDirection(-1);
        assertEquals(-1, worm.getFacingDirection());
    }

    @Test
    public void testSetFacingDirection() {
        worm.setFacingDirection(1);
        assertEquals(1, worm.getFacingDirection());

        worm.setFacingDirection(-1);
        assertEquals(-1, worm.getFacingDirection());

        worm.setFacingDirection(0);
        assertEquals(-1, worm.getFacingDirection());
    }

    @Test
    public void testUpdateFacingDirection() {
        worm.getBody().setVector(new Vector(1, 0));
        worm.updateFacingDirection();
        assertEquals(1, worm.getFacingDirection());

        worm.getBody().setVector(new Vector(-1, 0));
        worm.updateFacingDirection();
        assertEquals(-1, worm.getFacingDirection());

        worm.getBody().setVector(new Vector(0, 0));
        worm.updateFacingDirection();
        assertEquals(-1, worm.getFacingDirection());
    }

    @Test
    public void testStatusEffectsInitialization() {
        assertNotEquals(null, worm.getStatusEffects());
        assertEquals(0, worm.getStatusEffects().size());
    }

    @Test
    public void testAddStatusEffect() {
        StatusEffect effect = new PoisonEffect();
        worm.addStatusEffect(effect);
        assertEquals(1, worm.getStatusEffects().size());
        assertEquals(effect, worm.getStatusEffects().get(0));
    }

    @Test
    public void testRemoveStatusEffect() {
        StatusEffect effect = new PoisonEffect();
        worm.addStatusEffect(effect);
        worm.removeStatusEffect(effect);
        assertEquals(0, worm.getStatusEffects().size());
    }

    @Test
    public void testApplyStatusEffects() {
        double initialHealth = worm.getStats().getHp();
        PoisonEffect poison = new PoisonEffect();
        worm.addStatusEffect(poison);

        worm.applyStatusEffects();

        assertEquals(initialHealth - 1, worm.getStats().getHp());
        assertEquals(4, poison.getRemainingTurns());
    }

    @Test
    public void testStatusEffectExpiration() {
        double initialHealth = worm.getStats().getHp();
        PoisonEffect poison = new PoisonEffect();
        worm.addStatusEffect(poison);
        for (int i = 0; i < 5; i++) {
            worm.applyStatusEffects();
        }
        assertEquals(initialHealth - 5, worm.getStats().getHp());
        assertEquals(0, worm.getStatusEffects().size());
    }

    @Test
    public void testMultipleSameStatusEffectNotAdded() {
        PoisonEffect poison1 = new PoisonEffect();
        PoisonEffect poison2 = new PoisonEffect();
        worm.addStatusEffect(poison1);
        worm.addStatusEffect(poison2);
        assertEquals(1, worm.getStatusEffects().size());
    }

    @Test
    public void testApplyMultipleStatusEffects() {
        double initialHealth = worm.getStats().getHp();
        PoisonEffect poison = new PoisonEffect();
        BurnEffect burn = new BurnEffect();
        worm.addStatusEffect(poison);
        worm.addStatusEffect(burn);

        // Apply poison for 3 turns
        for (int i = 0; i < 3; i++) {
            worm.applyStatusEffects();
        }

        assertEquals(initialHealth - 9, worm.getStats().getHp());
        assertEquals(2, worm.getStatusEffects().size());
    }

    @Test
    public void testRemoveAllStatusEffects() {
        PoisonEffect poison = new PoisonEffect();
        BurnEffect burn = new BurnEffect();
        worm.addStatusEffect(poison);
        worm.addStatusEffect(burn);

        worm.removeAllStatusEffects();

        assertEquals(0, worm.getStatusEffects().size());
    }
}
