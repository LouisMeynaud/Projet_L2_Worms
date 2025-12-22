package com.lllmtb.engine.turn;

import java.util.Objects;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.events.EventListener;
import com.lllmtb.events.game.ToolSelectedEvent;
import com.lllmtb.events.game.WeaponSelectedEvent;
import com.lllmtb.events.move.GroundCheckCollision;
import com.lllmtb.events.move.MouseInputEvent;
import com.lllmtb.events.physics.ActualizePhysicsEvent;
import com.lllmtb.events.physics.PhysicsCollisionWithOtherEntityEvent;
import com.lllmtb.events.physics.PhysicsCollisionWithWaterOrExitMapEvent;
import com.lllmtb.events.physics.RemoveWormOfTeam;
import com.lllmtb.events.turn.TurnEndEvent;
import com.lllmtb.events.turn.TurnSkipEvent;
import com.lllmtb.events.turn.TurnStartEvent;
import com.lllmtb.events.turn.TurnStopEvent;
import com.lllmtb.events.turn.WormPlayable;
import com.lllmtb.events.ui.MoveEvent;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.Move;
import static com.lllmtb.physics.Move.move;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;

public final class TurnManager {

    private Turn turn;
    private final GameWorld world;

    private final long turnDurationInMs;
    private long remainingTimeInMs;

    public TurnManager(GameWorld world, long turnDuration) {
        this.world = world;
        this.turnDurationInMs = 1000 * turnDuration;
        this.init();
    }

    public GameWorld getWorld() {
        return this.world;
    }

    public long getTurnDuration() {
        return this.turnDurationInMs;
    }

    public long getRemainingTime() {
        return this.remainingTimeInMs;
    }

    public void init() {
        setupEvents();
    }

    public void startTurn(Team team) {
        this.remainingTimeInMs = turnDurationInMs;
        this.turn = new Turn(team);
        world.getBus().postQueued(new TurnStartEvent(team));
        Logger.logger.info("Start turn for the team {}\n", team.name);
        world.getBus().postQueued(new WormPlayable(turn.getWormPlayable()));
    }

    public void updateTurn(double elapsedTime) {
        this.remainingTimeInMs -= (long) elapsedTime;
        if (remainingTimeInMs <= 0) {
            endTurn();
        } else if (this.turn.getIsEnd()) {
            endTurn();
        }
    }

    private void endTurn() {
        this.turn.getTeam().emptyHand();
        this.turn.end();
        Logger.logger.info("End turn - switch Team\n");
        world.getBus().postQueued(new TurnEndEvent());
        world.nextTeamTurn();
    }

    public Turn getTurn() {
        return turn;
    }

    private void setupEvents() {
        // TODO - unsubscribe when stopping the gameworld. Note that we can keep these listeners across turns because they use dynamic data

        EventListener<TurnStopEvent> turnStopListener = event -> {
            this.turn.setIsEnd();
        };

        EventListener<TurnSkipEvent> turnskipEventListener = event -> {
            if (remainingTimeInMs < turnDurationInMs - 3000) {
                Logger.logger.info("Turn skipped by team {}", this.turn.getTeam().name);
                endTurn();
            }
        };

        EventListener<MoveEvent> moveListener = event -> {
            world.queueCommand(move(this.turn.getWormPlayable(), event.direction));
        };

        EventListener<GroundCheckCollision> groundCollisionListener = event -> {
            if (turn.getWormPlayable().uuid == event.worm.uuid) {
                Move.jumpToMax();
            }
        };

        EventListener<WeaponSelectedEvent> weaponSelectedEventEventListener = event -> {
            turn.getTeam().selectWeapon(event.getWeaponId());
        };

        EventListener<ToolSelectedEvent> toolSelectedEventEventListener = event -> {
            turn.getTeam().selectTool(event.getToolId());
        };

        EventListener<PhysicsCollisionWithWaterOrExitMapEvent> waterExitCollision = event -> {
            if (event.getMovingBody() instanceof Worm) {
                Worm worm = (Worm) event.getMovingBody();
                worm.damageWorm(worm.getStats().getHp());
                Logger.logger.info("Worm {} is dead !", worm.name);
                world.getBus().postQueued(new ActualizePhysicsEvent());
                if (worm.uuid == turn.getWormPlayable().uuid) {
                    world.getBus().postQueued(new TurnStopEvent());
                }
            }
        };

        EventListener<PhysicsCollisionWithOtherEntityEvent> hitCollision = event -> {
            if (event.getMovingBody() instanceof Projectile) {
                if (event.getHitBody() != null && event.getHitBody() instanceof Worm worm) {
                    Projectile bullet = (Projectile) event.getMovingBody();
                    worm.damageWorm(20);
                }
            }
            world.getBus().postQueued(new ActualizePhysicsEvent());
        };

        EventListener<ActualizePhysicsEvent> actualizePhysicsBodies = event -> {
            world.getPhysics().actualizeBodies();
        };

        EventListener<RemoveWormOfTeam> removeWormDeathInTeam = event -> {
            Worm worm = event.worm;
            worm.getTeam().removeWorm(worm);
        };

        EventListener<MouseInputEvent> mouseInputEventListener = event -> {
            PhysicsBody wormBody = turn.getWormPlayable().getBody();

            double mouseX = event.getPos().getX();
            double mouseY = event.getPos().getY();
            double wormX = wormBody.getPos().getX();
            double wormY = wormBody.getPos().getY();

            double centerX = wormX + (wormBody.getWidth() / 2.0);
            double centerY = wormY + (wormBody.getHeight() / 2.0);
            double deltaX = mouseX - centerX;
            double deltaY = (mouseY - centerY);
            double angle = Math.atan2(deltaY, deltaX);

            double dist = 1.5;
            double spawnX = centerX + (Math.cos(angle) * dist);
            double spawnY = centerY + (Math.sin(angle) * dist);

            Position spawnBullet = new Position(spawnX, spawnY);
            UseParams params = new UseParams(angle, spawnBullet, Objects.requireNonNull(turn.getWormPlayable()));

            System.out.println(turn.getTeam().getHand());
            world.queueCommands(turn.getTeam().use(params));
        };

        world.getBus().subscribe(TurnStopEvent.class, turnStopListener);
        world.getBus().subscribe(TurnSkipEvent.class, turnskipEventListener);

        world.getBus().subscribe(MoveEvent.class, moveListener);
        world.getBus().subscribe(GroundCheckCollision.class, groundCollisionListener);
        world.getBus().subscribe(WeaponSelectedEvent.class, weaponSelectedEventEventListener);
        world.getBus().subscribe(ToolSelectedEvent.class, toolSelectedEventEventListener);
        world.getBus().subscribe(MouseInputEvent.class, mouseInputEventListener);
        world.getBus().subscribe(PhysicsCollisionWithWaterOrExitMapEvent.class, waterExitCollision);
        world.getBus().subscribe(ActualizePhysicsEvent.class, actualizePhysicsBodies);
        world.getBus().subscribe(PhysicsCollisionWithOtherEntityEvent.class, hitCollision);
        world.getBus().subscribe(RemoveWormOfTeam.class, removeWormDeathInTeam);
    }
}
