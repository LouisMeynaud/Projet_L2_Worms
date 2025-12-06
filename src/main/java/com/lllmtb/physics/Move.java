package com.lllmtb.physics;

import com.lllmtb.engine.commands.AddPhysicsModifierCommand;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.ui.graphical.GamePanel.Direction;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * The {@code Move} class provides functionality to move a worm in different directions.
 * It manages the jump capability and creates movement commands based on the specified direction.
 */
public class Move {
    protected static int maxjump;
    protected static int jump = 0;

    /**
     * Resets the jump count to the maximum allowed jumps.
     */
    public static void jumpToMax() {
        jump = maxjump;
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
                System.out.println("Left move");
                yield v -> new Vector(-8, v.getY());

            case Right:
                System.out.println("Right move");
                yield v -> new Vector(8, v.getY());


            case Up:
                if (jump > 0) {
                    System.out.println("Up move");
                    jump--;
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

    public static void setMaxjump(int maxjump) {
        Move.maxjump = maxjump;
    }


}
