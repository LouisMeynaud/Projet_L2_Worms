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
        team = new Team("Team", 1, new Position[]{pos}, 0);
        worm = new Worm("TestWorm", team);
        Worm.setMaxHp(100);
    }

    @Test
    public void testPoisonEffectCreation() {
        PoisonEffect poison = new PoisonEffect();
        assertEquals(15, poison.getDamagePerTurn());
        assertEquals(3, poison.getRemainingTurns());
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
        double initialHealth = worm.getStats().getHp();
        PoisonEffect poison = new PoisonEffect();

        worm.addStatusEffect(poison);
        worm.applyStatusEffects();

        // Health should be reduced by 15
        assertEquals(initialHealth - 15, worm.getStats().getHp());
        // Poison duration should be decreased
        assertEquals(2, poison.getRemainingTurns());
    }

    @Test
    public void testPoisonMultipleTurns() {
        double initialHealth = worm.getStats().getHp();
        PoisonEffect poison = new PoisonEffect();

        worm.addStatusEffect(poison);

        // Apply poison for 5 turns
        for (int i = 0; i < 5; i++) {
            worm.applyStatusEffects();
        }

        // Total damage should be 15 (1 * 15)
        assertEquals(initialHealth - 45, worm.getStats().getHp());
        // Poison should be removed from effects list
        assertEquals(0, worm.getStatusEffects().size());
    }

    @Test
    public void testMultiplePoisonEffectsIsNotPossible() {
        double initialHealth = worm.getStats().getHp();
        PoisonEffect poison1 = new PoisonEffect();
        PoisonEffect poison2 = new PoisonEffect();

        worm.addStatusEffect(poison1);
        worm.addStatusEffect(poison2);

        assertEquals(1, worm.getStatusEffects().size());

        // Apply both poisons
        worm.applyStatusEffects();

        assertEquals(initialHealth - 15, worm.getStats().getHp());
    }
}