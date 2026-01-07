package com.lllmtb.engine.turn;

import java.util.Random;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.GameStateUpdatedEvent;
import com.lllmtb.events.game.WeaponSelectedEvent;
import com.lllmtb.events.input.MoveInputEvent;
import com.lllmtb.events.input.ProcessInputsEvent;
import com.lllmtb.events.input.UseInputEvent;
import com.lllmtb.events.turn.TurnEndEvent;
import com.lllmtb.events.turn.TurnStartEvent;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.PhysicsEngine;
import com.lllmtb.physics.Position;
import com.lllmtb.ui.graphical.GamePanel.Direction;
import com.lllmtb.engine.logger.Logger;

public class BotController {
    private final GameWorld world;
    private final Random random = new Random();

    private enum State {
        IDLE,
        WAITING_START,
        SELECT_WEAPON,
        MOVING,
        AIMING,
        SHOOTING,
        ENDING
    }

    private boolean active = false;

    private Team currentTeam;
    private Worm currentWorm;
    private State state = State.IDLE;
    private long stateTimer = 0;
    private long moveTimer = 0;
    private int shotsLeft;

    private Worm targetWorm;
    private Direction moveDirection;

    public BotController(GameWorld world) {
        this.world = world;
        subscribeEvents();
    }

    private void subscribeEvents() {
        EventBus bus = world.getBus();

        bus.subscribe(TurnStartEvent.class, event -> {
            if (event.team().isBot()) {
                active = true;
                currentTeam = event.team();
                shotsLeft = Turn.FIRE_LIMIT_PER_TURN;
                state = State.WAITING_START;
                stateTimer = 1000;
                Logger.logger.info("Bot logic started for team {}", currentTeam.name);
            } else {
                active = false;
                state = State.IDLE;
            }
        });

        bus.subscribe(TurnEndEvent.class, event -> {
            active = false;
            state = State.IDLE;
        });

        bus.subscribe(GameStateUpdatedEvent.class, event -> {
            if (active) update();
        });

        bus.subscribe(ProcessInputsEvent.class, event -> {
             if (active && state == State.MOVING) {
                 Logger.logger.info("? Bot processing move input: {}", moveDirection);
                 if (moveDirection != null) {
                     world.getBus().postQueued(new MoveInputEvent(moveDirection));
                 }
             }
        });
    }

    private long lastUpdate = System.currentTimeMillis();

    private void update() {
        long now = System.currentTimeMillis();
        long dt = now - lastUpdate;
        lastUpdate = now;

        if (stateTimer > 0) {
            stateTimer -= dt;
            return;
        }

        switch (state) {
            case WAITING_START:
                Logger.logger.info("? Bot waiting for current worm");
                 if (world.getPlayingTeam() != null && world.getPlayingTeam().getCurrentWorm() != null) {
                     currentWorm = world.getPlayingTeam().getCurrentWorm();
                     state = State.SELECT_WEAPON;
                     stateTimer = 500;
                 }
                 break;

            case SELECT_WEAPON:
                Logger.logger.info("? Bot selecting weapon");
                world.getBus().postQueued(new WeaponSelectedEvent(WeaponId.GUN));
                state = State.MOVING;
                stateTimer = 0;
                moveTimer = 2000;
                targetWorm = findNearestEnemy();
                break;

            case MOVING:
                Logger.logger.info("? Bot entering MOVING state, current target: {}", targetWorm != null ? targetWorm.uuid : "NONE");

                if (targetWorm == null || !targetWorm.isAlive()) {
                    targetWorm = findNearestEnemy();
                    Logger.logger.info("? Bot found new target worm {}", targetWorm != null ? targetWorm.uuid : "NONE");
                    if (targetWorm == null) {
                        state = State.ENDING;
                        return;
                    }
                }

                double myX = currentWorm.getBody().getPos().getX();
                double targetX = targetWorm.getBody().getPos().getX();
                double dist = Math.abs(targetX - myX);

                Logger.logger.info("? Bot distance to target: {} - Move timer: {}", dist, moveTimer);
                if (dist > 30 && moveTimer > 0) {
                    moveTimer -= dt;
                    moveDirection = targetX < myX ? Direction.Left : Direction.Right;
                    Logger.logger.info("? Bot decided to move {}", moveDirection);

                    // Random jump
                     if (random.nextInt(100) < 2) {
                         world.getBus().postQueued(new MoveInputEvent(Direction.Up));
                     }
                } else {
                    Logger.logger.info("? Bot finished moving");
                    moveDirection = null;
                    state = State.AIMING;
                    stateTimer = 1000;
                }
                break;

            case AIMING:
                Logger.logger.info("? Bot aiming at target worm {}", targetWorm != null ? targetWorm.uuid : "NONE");
                 state = State.SHOOTING;
                 stateTimer = 500;
                 break;

            case SHOOTING:
                Logger.logger.info("? Bot shooting at target worm {}", targetWorm != null ? targetWorm.uuid : "NONE");
                if (targetWorm != null && targetWorm.isAlive()) {
                    Position targetPos = targetWorm.getBody().getPos();
                    double errorX = (random.nextDouble() - 0.5) * 2;
                    double errorY = 2.5 + (random.nextDouble() - 0.5);

                    Position shootAt = new Position(targetPos.getX() + errorX, targetPos.getY() + errorY);
                    double chargeTime = 0;//500 + random.nextInt(1500);

                    world.getBus().postQueued(new UseInputEvent(shootAt, chargeTime));
                    Logger.logger.info("Bot shooting at {}", shootAt);
                    shotsLeft--;
                }
                if (shotsLeft > 0) {
                    Logger.logger.info("? Bot preparing for next shot, shots left: {}", shotsLeft);
                    state = State.MOVING;
                    stateTimer = 0;
                    moveTimer = 2000;
                } else {
                    Logger.logger.info("? No shots left, bot ending turn");
                    state = State.ENDING;
                    stateTimer = 2000;
                }
                break;

            default:
                break;
        }
    }

    private Worm findNearestEnemy() {
        return world.getPhysics().findClosestEnemyFromPosition(currentWorm.getBody().getPos(), currentTeam, currentWorm);
    }
}

