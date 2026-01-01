package com.lllmtb.physics;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;

import com.lllmtb.model.item.UseParams;
import com.lllmtb.model.item.tool.ParachuteTool;
import com.lllmtb.model.item.tool.ToolId;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * Represents a modifier that can be applied to the physics of an entity.
 */
public class PhysicsModifier {
    public final Transform transform;
    public final ItemId source;
    public double remaining;
    public final boolean oneShot;
    public boolean enabled;

    /**
     * Creates a new timed PhysicsModifier.
     * @param transform The transformation to apply.
     * @param source The source item ID of the modifier.
     * @param duration The duration in seconds for which the modifier is active (set to {@code Double.POSITIVE_INFINITY} for infinite duration).
     * @param enabled Whether the modifier is initially enabled.
     */
    public PhysicsModifier(@NonNull Transform transform, ItemId source, double duration, boolean enabled) {
        this.transform = Objects.requireNonNull(transform);
        this.source = source;
        this.remaining = duration;
        this.oneShot = false;
        this.enabled = enabled;
    }

    /**
     * Creates a new one-shot PhysicsModifier.
     * @param transform The transformation to apply.
     * @param source    The source item ID of the modifier.
     */
    public PhysicsModifier(@NonNull Transform transform, ItemId source) {
        this.transform = Objects.requireNonNull(transform);
        this.source = source;
        this.oneShot = true;
        this.enabled = true;
    }

    public List<GameCommand> onRemove(Worm owner) {
        if (this.source == ToolId.JETPACK) {
            ParachuteTool parachute = new ParachuteTool();
            UseParams useParams = new UseParams(owner);
            return parachute.use(useParams);
        }
        return null;
    }
}
