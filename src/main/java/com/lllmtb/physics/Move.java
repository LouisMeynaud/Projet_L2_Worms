package com.lllmtb.physics;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.EventListener;
import com.lllmtb.events.move.GroundCheckCollision;
import com.lllmtb.events.ui.graphical.*;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.ui.graphical.GamePanel.Direction;

public class Move {
    protected static  int maxjump= 2;
    protected static int jump = 0;

    public static void JumpToMax(){
        jump = maxjump;
    }

    public  static void move(Worm worm, Direction direction){
        switch (direction) {
            case Left: worm.getBody().setVector(worm.getBody().getVector().add(new Vector(-0.5, 0)));
                System.out.println("Left move");;
            break;
            case Right:  worm.getBody().setVector(worm.getBody().getVector().add(new Vector(0.5, 0)));
            System.out.println("Right move");;
            break;
            case Up:  if (jump > 0) {
                worm.getBody().setVector(worm.getBody().getVector().add(new Vector(0, 160)));
                System.out.println("Up move");;
                jump -= 1;
            }
            break;
            default: break;
        }
    }
}
