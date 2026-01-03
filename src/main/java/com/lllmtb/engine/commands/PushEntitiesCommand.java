package com.lllmtb.engine.commands;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * Command that pushes entities away from a position in a specific direction.
 * Used for melee attacks (e.g. baseball bat)
 */
public class PushEntitiesCommand implements GameCommand {
    private final Position position;
    private final double angleRadians;
    private final double pushRadius;
    private final double pushForce;
    private final Worm owner;

    /**
     * Creates a new PushEntitiesCommand.
     * @param position the center position of the push effect
     * @param angleRadians the angle of the swing in radians
     * @param pushRadius the radius within which entities will be pushed
     * @param pushForce the force of the push
     * @param owner the worm that initiated the push (will not be pushed)
     */
    public PushEntitiesCommand(@NonNull Position position, double angleRadians, double pushRadius, double pushForce, @NonNull Worm owner) {
        this.position = Objects.requireNonNull(position);
        this.angleRadians = angleRadians;
        this.pushRadius = pushRadius;
        this.pushForce = pushForce;
        this.owner = Objects.requireNonNull(owner);
    }

    @Override
    public void execute(GameWorld world) {
        // Calculate the swing direction from the angle
        double swingDirX = Math.cos(angleRadians);
        double swingDirY = Math.sin(angleRadians);

        for (PhysicsBody body : world.getPhysics().getBodies()) {
            if (body.getEntity() == owner) continue;

            Position bodyPos = body.getPos();
            double dx = bodyPos.getX() - position.getX();
            double dy = bodyPos.getY() - position.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= pushRadius && distance > 0) {
                double normalizedDx = dx / distance;
                double normalizedDy = dy / distance;
                double dotProduct = normalizedDx * swingDirX + normalizedDy * swingDirY;

                if (dotProduct > -0.5) { // 120° cone
                    Vector direction = new Vector(normalizedDx, normalizedDy);
                    double magnitude = Math.sqrt(swingDirX * swingDirX + swingDirY * swingDirY);
                    direction = direction.scale(1 / magnitude);

                    // entities closer get pushed harder
                    double distanceFactor = 1.0 - (distance / pushRadius);
                    double actualForce = pushForce * distanceFactor;
                    Vector pushVector = direction.scale(actualForce);
                    body.setVector(body.getVector().add(pushVector));

                    if (body.getEntity() instanceof Worm) {
                        ((Worm) body.getEntity()).setOnGround(false);
                    }
                }
            }
        }
    }
}

