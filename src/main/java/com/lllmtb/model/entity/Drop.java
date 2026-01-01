package com.lllmtb.model.entity;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.commands.AddPhysicsModifierCommand;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.events.EventListener;
import com.lllmtb.events.physics.GroundCheckCollision;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.PhysicsModifier;
import com.lllmtb.physics.Position;
import com.lllmtb.physics.Transform;
import com.lllmtb.physics.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Drop extends Entity {
    private PhysicsBody body;
    public final ItemId itemIdToUnlock;
    private static final double DROP_MAX_FALL_SPEED = -9.0;
    private CollisionType collisionType;

    public Drop(Position position, ItemId itemId) {
        this.itemIdToUnlock = itemId;
        this.body = new PhysicsBody(this, position, new Vector(0, 0), 0.1, 2, 2);
        this.collisionType = CollisionType.SLIDE;
    }

    @Override
    public CollisionType getCollisionType() {
        return this.collisionType;
    }

    @Override
    public PhysicsBody getBody() {
        return this.body;
    }

    public List<GameCommand> addModifier() {
        List<GameCommand> commands = new ArrayList<GameCommand>();
        Drop drop = this;

        Transform transform = (v) -> {
            double vy = v.getY();
            if (vy < 0) vy = Math.max(vy, DROP_MAX_FALL_SPEED);
            return new Vector(v.getX(), vy);
        };
        PhysicsModifier modifier = new PhysicsModifier(transform, null, Double.POSITIVE_INFINITY, true);

        commands.add(world -> world.getPhysics().add(drop.getBody()));
        commands.add(new AddPhysicsModifierCommand(this, modifier));

        commands.add(new GameCommand() {
            private final AtomicReference<EventListener<GroundCheckCollision>> dropRefListener = new AtomicReference<>();

            @Override
            public void execute(GameWorld world) {
                EventListener<GroundCheckCollision> groundListener = event -> {
                    if (event.worm().uuid == drop.uuid) {
                        PhysicsBody dropBody = drop.getBody();
                        world.getPhysics().remove(dropBody);
                        world.queueCommand(w -> w.getBus().unsubscribe(GroundCheckCollision.class, dropRefListener.get()));
                        drop.body = new PhysicsBody(drop, drop.getBody().getPos(), new Vector(0, 0), 0, 2, 2);
                        drop.collisionType = CollisionType.CROSS;
                        world.getPhysics().add(drop.getBody());
                    }
                };
                dropRefListener.set(groundListener);
                world.getBus().subscribe(GroundCheckCollision.class, groundListener);
            }
        });

        return commands;

    }
}

