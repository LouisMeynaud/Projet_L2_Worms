package com.lllmtb.model.entity.projectile;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.events.game.MapUpdateEvent;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.Position;
import com.lllmtb.terrain.map.Tile;

import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class TrapProjectile extends Projectile {
    private static final double force  = 100.0;
    private static final double mass   = 0.01;
    private static final double height = 1;
    private static final double width  = 1;
    private static final int damage = 0;

    public TrapProjectile(@NonNull UseParams params) {
        super(params, force * Math.min(params.duration / 100, 3) / 2, mass, height, width);
        }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.TOUCH;
    }

    @Override
    public boolean blocksTurnEnd() {
        return false;
    }

    @Override
    public List<GameCommand> onImpact(Entity entity) {
        if (entity == null) {
            List<GameCommand> list = new ArrayList<>();
            handleHit(list, entity, damage);
            list.add((world -> world.getMap().setTile(new Position(this.getBody().getPos().getX(), this.getBody().getPos().getY() - 1), Tile.TRAP)));
            list.add(world -> world.getBus().postQueued(new MapUpdateEvent(new Position(this.getBody().getPos().getX(), this.getBody().getPos().getY() - 1), 2)));
            list.add(delete());
            return list;
        }
            return null;
    }

    @Override
    public double getDamage() {
        return damage;
    }
}
