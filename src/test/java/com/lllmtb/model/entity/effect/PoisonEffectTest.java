package com.lllmtb.model.entity.effect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.Position;

/**
 * Test class for the poison effect system.
 */
public class PoisonEffectTest {

    private Worm worm;
    private Team team;

    @BeforeEach
    public void setUp() {
        Position pos = new Position(0, 0);
        team = new Team("Team", 1, new Position[]{pos});
        worm = new Worm("TestWorm", team);
        Worm.setMaxHp(100);
    }

    @Test
    public void testPoisonEffectCreation() {
        PoisonEffect poison = new PoisonEffect();
        assertEquals(1, poison.getDamagePerTurn());
        assertEquals(5, poison.getRemainingTurns());
        assertTrue(poison.isActive());
    }

    @Test
    public void testAddPoisonToWorm() {
        PoisonEffect poison = new PoisonEffect();

        worm.addStatusEffect(poison);

        assertEquals(1, worm.getStatusEffects().size());
    }

    @Test
    public void testPoisonDamageApplication() {
        int initialHealth = worm.getStats().getHp();
        PoisonEffect poison = new PoisonEffect();

        worm.addStatusEffect(poison);
        worm.applyStatusEffects();

        // Health should be reduced by 10
        assertEquals(initialHealth - 1, worm.getStats().getHp());
        // Poison duration should be decreased
        assertEquals(4, poison.getRemainingTurns());
    }

    @Test
    public void testPoisonMultipleTurns() {
        int initialHealth = worm.getStats().getHp();
        PoisonEffect poison = new PoisonEffect();

        worm.addStatusEffect(poison);

        // Apply poison for 5 turns
        for (int i = 0; i < 5; i++) {
            worm.applyStatusEffects();
        }

        // Total damage should be 5 (1 * 5)
        assertEquals(initialHealth - 5, worm.getStats().getHp());
        // Poison should be removed from effects list
        assertEquals(0, worm.getStatusEffects().size());
        // Poison should be removed after expiry
        assertEquals(0, worm.getStatusEffects().size());
    }

    @Test
    public void testMultiplePoisonEffectsIsNotPossible() {
        int initialHealth = worm.getStats().getHp();
        PoisonEffect poison1 = new PoisonEffect();
        PoisonEffect poison2 = new PoisonEffect();

        worm.addStatusEffect(poison1);
        worm.addStatusEffect(poison2);

        assertEquals(1, worm.getStatusEffects().size());

        // Apply both poisons
        worm.applyStatusEffects();

        // Damage should be 10 + 10 = 20
        assertEquals(initialHealth - 1, worm.getStats().getHp());
    }
}

