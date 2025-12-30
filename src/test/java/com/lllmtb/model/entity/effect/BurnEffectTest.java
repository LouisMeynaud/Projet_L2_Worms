package com.lllmtb.model.entity.effect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.Position;

/**
 * Test class for the burn effect system.
 */
public class BurnEffectTest {

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
    public void testBurnEffectCreation() {
        BurnEffect burn = new BurnEffect();
        assertEquals(1, burn.getDamagePerTurn());
        assertEquals(5, burn.getRemainingTurns());
        assertTrue(burn.isActive());
    }

    @Test
    public void testAddBurnToWorm() {
        BurnEffect burn = new BurnEffect();

        worm.addStatusEffect(burn);

        assertEquals(1, worm.getStatusEffects().size());
    }

    @Test
    public void testBurnDamageApplication() {
        int initialHealth = worm.getStats().getHp();
        BurnEffect burn = new BurnEffect();

        worm.addStatusEffect(burn);
        worm.applyStatusEffects();

        // Health should be reduced by 10
        assertEquals(initialHealth - 2, worm.getStats().getHp());
        // Poison duration should be decreased
        assertEquals(3, burn.getRemainingTurns());
    }

    @Test
    public void testBurnMultipleTurns() {
        int initialHealth = worm.getStats().getHp();
        BurnEffect burn = new BurnEffect();

        worm.addStatusEffect(burn);

        // Apply poison for 5 turns
        for (int i = 0; i < 5; i++) {
            worm.applyStatusEffects();
        }

        // Total damage should be 8 (2 * 4)
        assertEquals(initialHealth - 8, worm.getStats().getHp());
        // Burn should be removed from effects list
        assertEquals(0, worm.getStatusEffects().size());
    }

    @Test
    public void testMultipleBurnEffectsIsNotPossible() {
        int initialHealth = worm.getStats().getHp();
        BurnEffect burn1 = new BurnEffect();
        BurnEffect burn2 = new BurnEffect();

        worm.addStatusEffect(burn1);
        worm.addStatusEffect(burn2);

        assertEquals(1, worm.getStatusEffects().size());

        // Apply both poisons
        worm.applyStatusEffects();

        assertEquals(initialHealth - 2, worm.getStats().getHp());
    }
}