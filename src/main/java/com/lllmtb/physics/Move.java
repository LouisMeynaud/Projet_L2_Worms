package com.lllmtb.physics;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.graphicsUi.MoveEvent;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.ui.graphical.GamePanel.Direction;

public class Move {
    protected static  int maxsaut= 1;
    protected static int saut = 100;

    public  static void move(Worm worm, Direction direction){
        if(Collision.haveCollisionMap(worm.getBody().getPos())){
            saut = maxsaut;
        }
        switch (direction) {
            case Left: worm.getBody().setVector(worm.getBody().getVector().add(new Vector(-30, 0)));
                System.out.println("Left move");;
            break;
            case Right:  worm.getBody().setVector(worm.getBody().getVector().add(new Vector(30, 0)));
            System.out.println("Right move");;
            break;
            case Up:  if (saut >= 0) {
                worm.getBody().setVector(worm.getBody().getVector().add(new Vector(0, 160)));
                System.out.println("Up move");;
                saut -= 1;
            }


            System.out.println("Up move");;
            break;
            default: break;
        }
    }
}
