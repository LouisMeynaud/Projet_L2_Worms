package com.lllmtb.model.entity.projectile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterProjectileCommand;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.PhysicsEngine;
import com.lllmtb.physics.Position;

public final class TowerProjectile extends Projectile {
    private static final double FORCE  = 50;
    private static final double MASS   = 0.0060;
    private static final double HEIGHT = 2;
    private static final double WIDTH  = 1;
    private static final int DAMAGE    = 0;
    public static final int range = 25;

    private static final double FIRE_INTERVAL = 500;
    private long cooldownTimer = 0;
    private int creationTurnIndex = -1;


    public TowerProjectile(@NonNull UseParams params) {
        super(params, FORCE, MASS, HEIGHT, WIDTH);
    }

    @Override
    public CollisionType getCollisionType() {
        return CollisionType.TOUCH;
    }

    @Override
    public List<GameCommand> onImpact(Entity entity) {
        return new ArrayList<>();
    }

    @Override
    public List<GameCommand> update(long tickTime) {
        List<GameCommand> cmds = new ArrayList<>();

        this.cooldownTimer += tickTime;
        cmds.add(shooting(tickTime));

        return cmds;
    }

        public boolean blocksTurnEnd() {
        return false;
    }


    private GameCommand shooting(long tickTime) {
    return world -> {
            if (towerDeath(world)) return;
            if (world.getTurnManager().getRemainingTime() <= 0) {
                return;
            }
            if (this.cooldownTimer >= FIRE_INTERVAL) {
                this.cooldownTimer -= FIRE_INTERVAL;

                Team activeTeam = world.getTurnManager().getTurn().getTeam();
                if (Objects.equals(activeTeam, this.getOwner().getTeam())) return;

                List<PhysicsBody> activeBodies = activeTeam.getTeamWorms().stream().map(Worm::getBody).toList();

                Worm target = world.getPhysics().findClosestEnemyFromPosition(this.getBody().getPos(), this.getOwner().getTeam(), this.getOwner(), activeBodies);
                Position myPos = this.getBody().getPos();
                Position targetPos = target.getBody().getPos();

                double dx = myPos.getX() - targetPos.getX();
                double dy = myPos.getY() - targetPos.getY();
                double distSq = (dx * dx) + (dy * dy);
                if (distSq <= (range * range)) {
                    fireAtTarget(world, target, myPos);
                }
            }
        };
    }

private boolean towerDeath(GameWorld world) {
    int currentTurnIndex = world.getTurnManager().getNbTurn();

    if (this.creationTurnIndex == -1) {
        this.creationTurnIndex = currentTurnIndex;
    }
    if (currentTurnIndex - this.creationTurnIndex > world.getTeams().size() - 1) {
        world.queueCommand(this.delete());
        return true;
    }
    return false;
}



    private void fireAtTarget(GameWorld world, Worm target, Position myPos) {
    Position spawnPos = new Position(myPos.getX(),  myPos.getY() + (HEIGHT / 2.0));
    double angle = PhysicsEngine.calculateAngleToBody(spawnPos, target.getBody());
    UseParams bulletParams = new UseParams(angle, spawnPos, getOwner(), 0);
    world.queueCommand(new RegisterProjectileCommand(new BabyTowerProjectile(bulletParams), getOwner()));
}

    @Override
    public int getDamage() {
        return DAMAGE;
    }
}