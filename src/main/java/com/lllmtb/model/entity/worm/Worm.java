package com.lllmtb.model.entity.worm;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Vector;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

public class Worm extends Entity {
    private final PhysicsBody body;

    public Worm(@NonNull Position spawn){
        super();
        Objects.requireNonNull(spawn);
        this.body = new PhysicsBody(this, spawn, new Vector(), 100, 1, 1);
    }

    public PhysicsBody getBody(){
        return this.body;
    }
    
}
