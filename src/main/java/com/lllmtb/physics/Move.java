package com.lllmtb.physics;

import com.lllmtb.engine.logger.Logger;
import com.lllmtb.engine.commands.AddPhysicsModifierCommand;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.ui.graphical.GamePanel.Direction;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * The {@code Move} class provides functionality to move a worm in different directions.
 * It manages the jump capability and creates movement commands based on the specified direction.
 */
public class Move {
    protected static volatile int maxjump;
    protected static final AtomicInteger jump = new AtomicInteger(0);

    /**
     * Resets the jump count to the maximum allowed jumps.
     */
    public static void jumpToMax() {
        jump.set(maxjump);
    }

    /**
     * Creates a movement command for the specified worm in the given direction.
     * @param worm      The worm to move.
     * @param direction The direction to move the worm.
     * @return A {@link GameCommand} representing the movement, or {@code null} if no movement is possible.
     */
    public static GameCommand move(@NonNull Worm worm, @NonNull Direction direction) {
        Objects.requireNonNull(worm);
        Transform t = switch (Objects.requireNonNull(direction)) {
            case Left:
                Logger.logger.debug("Worm {} : Left move ", worm);
                yield v -> new Vector(v.getX() - 1, v.getY());

            case Right:
                Logger.logger.debug("Worm {} : Right move ", worm);
                yield v -> new Vector(v.getX() + 1, v.getY());

            case Up:
                if (jump.get() > 0) {
                    Logger.logger.debug("Worm {} in team : Up move ", worm);
                    jump.decrementAndGet();
                    worm.setOnGround(false); // Mark worm as not on ground when jumping
                    yield v -> new Vector(v.getX(), 20);
                }
                yield null;

            default:
                yield null;
        };
        if (t == null) {
            return null;
        }

        return new AddPhysicsModifierCommand(worm, new PhysicsModifier(t, null));
    }

    /**
     * set the number of max jump.
     * @param maxjump the maximum number of jumps we want to set
     */
    public static void setMaxjump(int maxjump) {
        Move.maxjump = maxjump;
    }

    /**
     *
     * @return the maximum jump
     */
    public static int getMaxjump() {
        return maxjump;
    }
}
