package com.lllmtb.physics;

import com.lllmtb.engine.commands.AddPhysicsModifierCommand;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.ui.graphical.GamePanel.Direction;

public class Move {
    protected static int maxjump = 2;
    protected static int jump = 0;

    public static void JumpToMax() {
        jump = maxjump;
    }

    public static GameCommand move(Worm worm, Direction direction) {
        Transform t = switch (direction) {
            case Left:
                System.out.println("Left move");
                yield v -> v.add(new Vector(-7, 0));

            case Right:
                System.out.println("Right move");
                yield v -> v.add(new Vector(7, 0));

            case Up:
                if (jump > 0) {
                    System.out.println("Up move");
                    jump--;
                    yield v -> v.add(new Vector(0, 60));
                }
                yield null;

            default:
                yield null;
        };
        if (t == null) return null;

        return new AddPhysicsModifierCommand(worm, new PhysicsModifier(t, null));
    }
}
