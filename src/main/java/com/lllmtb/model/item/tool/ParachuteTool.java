package com.lllmtb.model.item.tool;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.commands.AddPhysicsModifierCommand;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RemovePhysicsModifierCommand;
import com.lllmtb.events.EventListener;
import com.lllmtb.events.physics.GroundCheckCollision;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsModifier;
import com.lllmtb.physics.Transform;
import com.lllmtb.physics.Vector;
import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class ParachuteTool extends Tool {
    private static final double PARACHUTE_MAX_FALL_SPEED = -3.0;

    @Override
    public ToolId id() {
        return ToolId.PARACHUTE;
    }

    @Override
    public List<GameCommand> use(@NonNull UseParams params) {
        Objects.requireNonNull(params);

        List<GameCommand> commands = new ArrayList<>();

        Transform transform = (v) -> {
            double vy = v.getY();
            if (vy < 0) vy = Math.max(vy, PARACHUTE_MAX_FALL_SPEED);
            return new Vector(v.getX(), vy);
        };
        PhysicsModifier modifier = new PhysicsModifier(transform, id(), Double.POSITIVE_INFINITY, true);
        commands.add(new AddPhysicsModifierCommand(params.owner, modifier));

        commands.add(new GameCommand() {
            private final AtomicReference<EventListener<GroundCheckCollision>> listenerRef = new AtomicReference<>();

            @Override
            public void execute(GameWorld world) {
                EventListener<GroundCheckCollision> groundListener = event -> {
                    if (event.worm().uuid == params.owner.uuid) {
                        world.queueCommand(new RemovePhysicsModifierCommand(params.owner, modifier));
                        world.queueCommand(w -> w.getBus().unsubscribe(GroundCheckCollision.class, listenerRef.get()));
                    }
                };
                listenerRef.set(groundListener);
                world.getBus().subscribe(GroundCheckCollision.class, groundListener);
            }
        });

        return commands;
    }
}
