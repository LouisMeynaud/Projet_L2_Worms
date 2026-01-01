package com.lllmtb.physics;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.tool.ToolId;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class PhysicsModifierTest {
    private final Transform transform = v -> new Vector(v.getY(), v.getX());
    private final ItemId source = ToolId.PARACHUTE;
    private double remaining = 10.0;
    private boolean enabled;

    @Test
    public void testPhysicsModifierCreation() {
        PhysicsModifier modifierWithDuration = new PhysicsModifier(transform, source, remaining, true);
        assertSame(transform, modifierWithDuration.transform);
        assertSame(source, modifierWithDuration.source);
        assertEquals(remaining, modifierWithDuration.remaining, 0.0);
        assertFalse(modifierWithDuration.oneShot);
        assertTrue(modifierWithDuration.enabled);
    
        PhysicsModifier oneShotModifier = new PhysicsModifier(transform, source);
        assertSame(transform, oneShotModifier.transform);
        assertSame(source, oneShotModifier.source);
        assertTrue(oneShotModifier.oneShot);
        assertTrue(oneShotModifier.enabled);
    }

    @Test
    public void testOnRemove(){
        PhysicsModifier modifierJetpack = new PhysicsModifier(transform, ToolId.JETPACK, remaining, true);
        Worm worm = new Worm("Test", Mockito.mock(Team.class));
        assertNotNull(modifierJetpack.onRemove(worm));
    }

    @Test
    public void testOnRemoveNotJetpack(){
        PhysicsModifier modifierJetpack = new PhysicsModifier(transform, source, remaining, true);
        Worm worm = new Worm("Test", Mockito.mock(Team.class));
        assertNull(modifierJetpack.onRemove(worm));
    }
}