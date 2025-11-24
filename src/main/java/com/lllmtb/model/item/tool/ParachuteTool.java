package com.lllmtb.model.item.tool;

import com.lllmtb.engine.commands.AddPhysicsModifierCommand;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.physics.PhysicsModifier;
import com.lllmtb.physics.Vector;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class ParachuteTool extends Tool {

    @Override
    public ToolId id() {
        return ToolId.PARACHUTE;
    }

    @Override
    public List<GameCommand> use(@NonNull UseParams params) {
        Objects.requireNonNull(params);
        PhysicsModifier parachute = (currentVel) -> {
            double vy = currentVel.getY();
            if (vy < 0) {
                vy = Math.max(vy, -3.0); // cap fall speed - example
            }
            return new Vector(currentVel.getX(), vy);
        };

        GameCommand cmd = new AddPhysicsModifierCommand(params.owner, parachute, -1);
        return List.of(cmd);
    }
}
