package com.lllmtb.physics;

import java.util.concurrent.CopyOnWriteArrayList;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.CoinCollisionEvent;
import com.lllmtb.events.physics.GroundCheckCollision;
import com.lllmtb.model.entity.Coin;
import com.lllmtb.model.entity.CollisionType;
import com.lllmtb.model.entity.projectile.CatConcreteProjectile;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.terrain.map.Tile;

/**
 * The {@code Collision} class is the central collision engine for the game, inspired
 * by JBump principles (swept collision detection).
 * Handles collision detection for Entity and {@link Map} (Circle-AABB) and
 * between Entities (Circle-Circle).
 */
public class Collision {

    // The game map
    private final Map map;
    // The list of active PhysicsBodies in the game
    private final CopyOnWriteArrayList<PhysicsBody> bodies;
    // Event bus for posting some collision events
    private final EventBus eventBus;
    // Small epsilon value to prevent sticking on surfaces
    private static final double EPSILON = 0.002;

    /**
     * Constructor of Collision, with the map and the entities to check
     * @param m the {@link Map} of the game
     * @param b the {@link CopyOnWriteArrayList} containing active Entity's {@link PhysicsBody}.
     * @param eventBus the {@link EventBus} to post collision events
     */
    public Collision(Map m, CopyOnWriteArrayList<PhysicsBody> b, EventBus eventBus) {
        this.map = m;
        this.bodies = b;
        this.eventBus = eventBus;
    }

    /**
     * As in JBump, the {@code Impact} class allows to stack information from the collision test.
     */
    public static class Impact {

        // The hit body (null if it's the map)
        public final PhysicsBody hitBody;
        // The exact position of the collision (= finalPos if no collision)
        public final Position collisionPos;
        // Time of impact (0.0 if immediately, 1.0 if no collision)
        public final double ti;
        // The normal vector of the collision
        public final Vector normal;
        // true if a collision is confirmed
        public final boolean collided;
        // true if the body has to die on impact (e.g., out of map, water)
        public final boolean die;

        /**
         * Constructor of Impact when there isn't any collision
         * @param pos the {@link Position} that is the final position
         */
        public Impact(Position pos) {
            this.hitBody = null;
            this.collisionPos = pos;
            this.ti = 1.0;
            this.normal = new Vector(0, 0);
            this.collided = false;
            this.die = false;
        }

        /**
         * Constructor of impact when there is a collision
         * @param ti the {@code double} which represents the time of impact, between {@code 0.0} and {@code 1.0}.
         * @param vector the {@link Vector} of the movement.
         * @param body the {@link PhysicsBody} of the entity in movement.
         * @param normal the normal {@link Vector} of the hit structure.
         * @param hitBody the {@link PhysicsBody} of the collided entity ({@code null} if it's the map).
         * @param die true if the body has to die on impact (e.g., out of map, water), false otherwise.
         */
        public Impact(double ti, Vector vector, PhysicsBody body, Vector normal, PhysicsBody hitBody, boolean die) {
            this.hitBody = hitBody;
            this.collisionPos = vector.newPos(body.getPos(), ti);
            this.ti = ti;
            this.normal = normal;
            this.collided = true;
            this.die = die;
        }
    }

    /**
     * <p>
     * Simulates the displacement of a {@link PhysicsBody} based on the position of it
     * and the final position without collision, returning an {@link Impact} with
     * the information about possible collisions.
     * Detects collisions with the map and other {@link PhysicsBody} using
     * swept circle vs AABB and swept circle vs circle algorithms.
     * Special cases that are handled:
     * - Worms, which have two circles for hitbox (body and head)
     * - Coins, which has collision only with map and worms and trigger a collision event but do not block movement when it's with a worm
     * - CatConcreteProjectile, which only collides with the map
     * </p>
     * @param body the {@link PhysicsBody} in movement
     * @param endPos the final {@link Position} of {@code body} without collision.
     * @return an {@link Impact} with the information about possible collisions
     */
    public Impact move(PhysicsBody body, Position endPos) {
        // Initialize final impact as no collision
        Impact finalImpact = new Impact(endPos);
        Vector vector = new Vector(endPos.getX() - body.getPos().getX(), endPos.getY() - body.getPos().getY());

        // if the body has to cross everything to make it disappear if it goes out of map
        if (body.getEntity().getCollisionType() == CollisionType.CROSS) {
            Impact i = this.testSweptCircleTile(body, body.getPos(), body.getWidth() / 2, endPos, vector);
            if (i.die) return i;
            return finalImpact;
        }

        // if the body is a worm
        if (body.getEntity() instanceof Worm) {
            // Calculation of the two circles radius
            double radius = body.getWidth() / 2;
            Position wormHead = new Position(body.getPos().getX(), body.getPos().getY() + 1.0);
            Position endPos2 = new Position(endPos.getX(), endPos.getY() + 1.0);
            // test collision for the two circles of the worm
            Impact mapImpact1 = this.testSweptCircleTile(body, body.getPos(), radius, endPos, vector);
            Impact mapImpact2 = this.testSweptCircleTile(body, wormHead, radius, endPos2, vector);
            if (mapImpact1.ti < finalImpact.ti) {
                finalImpact = mapImpact1;
            }
            if (mapImpact2.ti < finalImpact.ti) {
                finalImpact = mapImpact2;
            }
            // test with other bodies on the map without other worms
            for (PhysicsBody other : this.bodies) {
                if ((body != other) && !(other.getEntity() instanceof Worm)) {

                    // Special case for coins
                    if (other.getEntity() instanceof Coin) {
                        double radiusOther = other.getWidth() / 2;
                        Position otherPos = new Position(other.getPos().getX() + 0.5, other.getPos().getY() + 0.5);
                        Impact coinImpact = this.testSweptCircleCircle(body, body.getPos(), radius, other, otherPos, radiusOther, endPos, vector);
                        if (coinImpact.ti < finalImpact.ti) { // Collision with coin
                            eventBus.postQueued(new CoinCollisionEvent((Coin) other.getEntity(), ((Worm) body.getEntity()).getTeam()));
                        }
                        continue;
                    }
                    double radiusOther = other.getHeight() / 2;
                    Position otherPos = new Position(other.getPos().getX() + 0.5, other.getPos().getY() + 0.5);
                    Impact bodyImpact = this.testSweptCircleCircle(body, body.getPos(), radius, other, otherPos, radiusOther, endPos, vector);
                    if (bodyImpact.ti < finalImpact.ti) {
                        finalImpact = bodyImpact;
                    }
                    Impact bodyImpact2 = this.testSweptCircleCircle(body, wormHead, radius, other, otherPos, radiusOther, endPos2, vector);
                    if (bodyImpact2.ti < finalImpact.ti) {
                        finalImpact = bodyImpact2;
                    }
                }
            }
        // the special case of cat concrete projectile
        } else if (body.getEntity() instanceof CatConcreteProjectile) {
            double radius = body.getHeight() / 2;
            // Entity-Map Test: searches for the earliest impact time
            Impact mapImpact = this.testSweptCircleTile(body, body.getPos(), radius, endPos, vector);
            if (mapImpact.collided && mapImpact.ti < finalImpact.ti) {
                finalImpact = mapImpact;
            }
        } else {
            // if the body is not a worm
            double radius = body.getHeight() / 2;
            // Entity-Map Test: searches for the earliest impact time
            Impact mapImpact = this.testSweptCircleTile(body, body.getPos(), radius, endPos, vector);
            if (mapImpact.collided && mapImpact.ti < finalImpact.ti) {
                finalImpact = mapImpact;
            }

            // Entity-Entity Test: iterates over all other bodies to find the earliest impact time
            for (PhysicsBody other : this.bodies) {
                if (other != body && !(other.getEntity() instanceof Coin)) {
                    double radiusOther = other.getWidth() / 2;
                    Position otherPos = new Position(other.getPos().getX() + 0.5, other.getPos().getY() + 0.5);

                    Impact bodyImpact = this.testSweptCircleCircle(body, body.getPos(), radius, other, otherPos, radiusOther, endPos, vector);
                    if (bodyImpact.ti < finalImpact.ti) {

                        finalImpact = bodyImpact;
                    }
                    // Special case for worms with two circles
                    if (other.getEntity() instanceof Worm) {
                        Position wormHead = new Position(other.getPos().getX() + 0.5, other.getPos().getY() + 1.5);
                        bodyImpact = this.testSweptCircleCircle(body, body.getPos(), radius, other, wormHead, radiusOther, endPos, vector);
                        if (bodyImpact.ti < finalImpact.ti) {

                            finalImpact = bodyImpact;
                        }
                    }
                }
            }
        }

        return finalImpact;
    }

    /**
     * Calculates the first swept impact between the moving body and the map's solid tiles.
     * @param body The moving {@link PhysicsBody}.
     * @param startPos the {@link Position} of the body center
     * @param radius the radius of the body hitbox circle
     * @param endPos The target {@link Position}.
     * @param vector The displacement {@link Vector}.
     * @return The nearest impact with a solid tile.
     */
    private Impact testSweptCircleTile(PhysicsBody body, Position startPos, double radius, Position endPos, Vector vector) {
        Position startPos2 = new Position(startPos.getX() + radius, startPos.getY() + radius);
        Vector displacement = body.getVector();

        Impact nearestImpact = new Impact(endPos);

        // Determine the expanded AABB area on the grid to check
        int minX = (int) Math.floor(Math.min(startPos.getX(), endPos.getX()));
        int maxX = (int) Math.ceil(Math.max(startPos.getX(), endPos.getX()));
        int minY = (int) Math.floor(Math.min(startPos.getY(), endPos.getY()));
        int maxY = (int) Math.ceil(Math.max(startPos.getY(), endPos.getY()));

        // Iterate over the tiles in the expanded AABB
        for (int tx = minX; tx <= maxX; tx++) {
            for (int ty = minY; ty <= maxY; ty++) {
                Position tilePos = new Position(tx, ty);

                // Handle CROSS collision type (out of map)
                if (body.getEntity().getCollisionType() == CollisionType.CROSS) {
                    if (this.map.inMap(tilePos)) {
                        continue;
                    } else {
                        double[] result = this.sweptCircleAABB(startPos2, radius, displacement, tilePos);
                        double ti = result[0];
                        Vector normal = new Vector(result[1], result[2]);
                        if (ti >= 0.0 && ti < 1.0 && ti < nearestImpact.ti) {
                            nearestImpact = new Impact(ti, vector, body, normal, null, true);
                        }
                        return nearestImpact;
                    }
                }

                // Skip non-solid tiles
                if ((this.map.inMap(tilePos) || (this.map.inMap(new Position(tx, 0)) && !this.map.inMap(tilePos)))
                        && !this.map.isSolid(tilePos) && !this.map.isTileAt(new Position(tx, ty), Tile.WATER)) {
                    continue; // Skip non-solid tiles
                }

                // Immediate overlap test (ti=0.0)
                if (isColliding(startPos2, radius, tilePos)) {
                    Vector normal = this.calculateTileNormal(startPos2, tilePos);
                    if ((this.map.isTileAt(new Position(tx, ty), Tile.WATER)) || !(this.map.inMap(new Position(tx, ty)))) {
                        return new Impact(0.0, vector, body, normal, null, true);
                    } else {
                        return new Impact(0.0, vector, body, normal, null, false);
                    }
                }

                // Swept AABB test
                double[] result = this.sweptCircleAABB(startPos2, radius, displacement, tilePos);
                double ti = result[0];
                Vector normal = new Vector(result[1], result[2]);

                if (ti >= 0.0 && ti < 1.0 && ti < nearestImpact.ti) {
                    if ((this.map.isTileAt(new Position(tx, ty), Tile.WATER)) || !(this.map.inMap(new Position(tx, ty)))) {
                        nearestImpact = new Impact(ti, vector, body, normal, null, true);
                    } else {
                        nearestImpact = new Impact(ti, vector, body, normal, null, false);
                    }
                }
            }
        }

        return nearestImpact;
    }

    /**
     * Calculates the first swept impact between two circular bodies.
     * Reduces the problem to a Swept Point vs. Static Circle of combined radius.
     * @param body1 The moving {@link PhysicsBody}.
     * @param startPos1 the {@link Position} of the first body center
     * @param radius1 the radius of the first body hitbox circle
     * @param body2 The target {@link PhysicsBody} (static for this calculation).
     * @param startPos2 the {@link Position} of the second body center
     * @param radius2 the radius of the second body hitbox circle
     * @param endPos The target {@link Position} of body1.
     * @param vector The displacement {@link Vector} of body1.
     * @return The nearest impact with body2.
     */
    private Impact testSweptCircleCircle(PhysicsBody body1, Position startPos1,
                                         double radius1, PhysicsBody body2, Position startPos2, double radius2, Position endPos, Vector vector) {
        Position startPos = new Position(startPos1.getX() + radius1, startPos1.getY() + radius1);
        Vector displacement = new Vector(endPos.getX() - startPos1.getX(), endPos.getY() - startPos1.getY());

        double combinedRadius = radius1 + radius2;

        // Use quadratic formula resolution to find ti
        double ti = sweptPointCircle(startPos, displacement, startPos2, combinedRadius);

        if (ti >= 0.0 && ti < 1.0) {
            // Calculate the exact impact position
            Position hitPos = vector.newPos(startPos1, ti);

            // Calculate the normal vector: from body2's center to body1's center
            Vector normalVec = hitPos.sub(body2.getPos());
            double length = Math.sqrt(normalVec.getX() * normalVec.getX() + normalVec.getY() * normalVec.getY());
            Vector normal = normalVec.scale(1.0 / length);

            return new Impact(ti, vector, body1, normal, body2, false);
        }

        return new Impact(endPos);
    }

    /**
     * Static collision detection (overlap) between a circle and a tile (AABB).
     * Used for the initial overlap test (ti=0.0).
     * @param circleCenter Center {@link Position} of the circle.
     * @param radius Radius of the circle.
     * @param tilePos Bottom-left corner of the tile (1x1 square).
     * @return True if the circle overlaps the tile.
     */
    private boolean isColliding(Position circleCenter, double radius, Position tilePos) {
        double cx = circleCenter.getX();
        double cy = circleCenter.getY();
        double ax = tilePos.getX();
        double ay = tilePos.getY();

        // Finds the closest point on the AABB to the circle center (Clamping)
        double closestX = Math.max(ax, Math.min(cx, ax + 1.0));
        double closestY = Math.max(ay, Math.min(cy, ay + 1.0));

        double distanceX = Math.abs(cx - closestX);
        double distanceY = Math.abs(cy - closestY);

        double distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);

        return distanceSquared <= (radius * radius);
    }

    /**
     * Calculates the impact normal for a circle hitting a tile.
     * @param hitPos Center {@link Position} of the circle at the moment of impact.
     * @param tilePos The {@link Position} of the tile.
     * @return The unit normal vector.
     */
    private Vector calculateTileNormal(Position hitPos, Position tilePos) {
        double cx = hitPos.getX();
        double cy = hitPos.getY();
        double tx = tilePos.getX() + 0.5;
        double ty = tilePos.getY() + 0.5;

        // The normal vector is the vector from the closest point to the circle center
        Vector normalVec = new Vector(cx - tx, cy - ty);

        // Normalization
        double length = Math.sqrt(normalVec.getX() * normalVec.getX() + normalVec.getY() * normalVec.getY());
        if (length == 0.0) {
            return new Vector(0, 0);
        }

        return normalVec.scale(1.0 / length);
    }

    /**
     * Calculates the Time of Impact (ti) and the normal {@link Vector} between a circle Tile (AABB).
     * This is the "Swept Point vs Enlarged AABB" algorithm.
     * @param startPos Starting {@link Position}.
     * @param radius Radius of the circle.
     * @param displacement Total displacement {@link Vector}.
     * @param tilePos Tile corner {@link Position}.
     * @return double[] {ti, normalX, normalY}. ti=1.0 if no collision.
     */
    private double[] sweptCircleAABB(Position startPos, double radius, Vector displacement, Position tilePos) {
        // Creates the enlarged AABB (expanded by the radius)
        double tileX = tilePos.getX() + radius;
        double tileY = tilePos.getY() + radius;

        double dx = displacement.getX();
        double dy = displacement.getY();
        double x = startPos.getX();
        double y = startPos.getY();

        double txNearX;
        double txFarX;
        double tyNearY;
        double tyFarY;
        // Calculate Near (entry) and Far (exit) times on X and Y axes
        if (dx == 0) {
            txNearX = 0;
            txFarX = 1;
        } else {
            txNearX = (tileX - 1.0 - x) / dx;
            txFarX = (tileX + 1.0 - x) / dx;
        }

        if (dy == 0) {
            tyNearY = 0;
            tyFarY = 1;
        } else {
            tyNearY = (tileY - 1.0 - y) / dy;
            tyFarY = (tileY + 1.0 - y) / dy;
        }

        // Invert times if direction is negative
        if (dx < 0) {
            double temp = txNearX;
            txNearX = txFarX;
            txFarX = temp;
        }
        if (dy < 0) {
            double temp = tyNearY;
            tyNearY = tyFarY;
            tyFarY = temp;
        }

        // tiNear is the moment both X and Y conditions are met (take the max)
        double tiNear = Math.max(txNearX, tyNearY);
        // tiFar is the moment the body leaves the area (take the min)
        double tiFar = Math.min(txFarX, tyFarY);

        // Check impact validity
        if (tiNear >= 1.0 || tiNear < 0.0 || tiNear >= tiFar) {
            return new double[] {1.0, 0, 0};
        }

        // Determine the normal (main axis of impact)
        double normalX = 0;
        double normalY = 0;
        if (txNearX > tyNearY) {
            // Impact occurs on the X-axis (vertical wall)
            normalX = (dx > 0) ? -1 : 1;
        } else {
            // Impact occurs on the Y-axis (ground or ceiling)
            normalY = (dy > 0) ? -1 : 1;
        }

        return new double[] {tiNear, normalX, normalY};
    }

    /**
     * <p>
     * Calculate the time of impact between two Entities by using the {@link Position}
     * of the two and the quadratic formula resolution. This quadratic resolution is looking
     * when distance between centers at time t = combined radius
     * </p>
     * @param p1 the {@link Position} of the first {@link PhysicsBody}'s hitbox center
     * @param v1 the {@link Vector} of the displacement
     * @param c2 the {@link Position} of the second {@link PhysicsBody}'s hitbox center
     * @param r2 the sum of the radius of the two static circles
     * @return {@code ti} or {@code 1.0} if no collision occurs.
     */
    private double sweptPointCircle(Position p1, Vector v1, Position c2, double r2) {
        Vector p1ToC2 = c2.sub(p1);
        // Coefficients for the quadratic equation 1*t^2 + B*t + C = 0
        // A = ||V1||^2
        double a = v1.getX() * v1.getX() + v1.getY() * v1.getY();
        // B = 2 * (V1.p1toC2)
        double b = -2.0 * (v1.getX() * p1ToC2.getX() + v1.getY() * p1ToC2.getY());
        // C = ||p1toC2||^2 - r2^2
        double c = p1ToC2.getX() * p1ToC2.getX() + p1ToC2.getY() * p1ToC2.getY() - r2 * r2;
        // if a = 0, object1 not in movement, so no collision
        if (a == 0.0) {
            return 1.0;
        }
        // Calculation of the discriminant
        double discriminant = b * b - 4 * a * c;
        // if discriminant < 0, so no collision
        if (discriminant < 0.0) {
            return 1.0;
        }
        // Calculation of solution, only the one with the "-" because it's the first
        double ti = (-b - Math.sqrt(discriminant)) / (2.0 * a);
        if (ti >= 0.0 && ti < 1.0) {
            return ti;
        }
        return 1.0;
    }

    /**
     * <p>
     * Simulates the case when the {@link PhysicsBody} has to slide along the hit object/cliff
     * it annulates the vector detail in collision
     * </p>
     * @param body the {@link PhysicsBody} which move
     * @param impact the {@link Impact} with collision's information
     * @param ticktime the duration of a simulation tick
     * @param world the {@link GameWorld} of the collisions
     */
    public void slide(PhysicsBody body, Impact impact, double ticktime, GameWorld world) {
        Position safePos = new Position(
                impact.collisionPos.getX() + impact.normal.getX() * EPSILON,
                impact.collisionPos.getY() + impact.normal.getY() * EPSILON
        );
        body.setPos(safePos);

        // if the collision is with a wall
        if (impact.normal.getY() == 0) {
            body.setVector(new Vector(0, body.getVector().getY()));
            // Mark worm is not on ground when hitting a wall
            if (body.getEntity() instanceof Worm) {
                ((Worm) body.getEntity()).setOnGround(false);
            }
        } else {
            if (body.getEntity() instanceof Worm worm) {
                world.queueCommand(PhysicsEngine.fallDamage(worm));
            }
            body.setVector(new Vector(body.getVector().getX(), 0));
            // if the worm touch the ground, it can jump again
            if (impact.normal.getY() == 1) {
                if (body.getEntity() instanceof Worm) {
                    ((Worm) body.getEntity()).setOnGround(true);
                }
                world.getBus().postQueued(new GroundCheckCollision(body.getEntity()));
            } else if (impact.normal.getY() == -1) {
                // Hit ceiling - mark as not on ground
                if (body.getEntity() instanceof Worm) {
                    ((Worm) body.getEntity()).setOnGround(false);
                }
            }
        }
        double ti = ticktime - (ticktime * impact.ti);
        // calculate the new position
        Position nextPos = body.getVector().newPos(body.getPos(), ti);
        Impact i = move(body, nextPos);

        if (i.collided) {
            safePos = new Position(i.collisionPos.getX() + i.normal.getX() * EPSILON, i.collisionPos.getY() + i.normal.getY() * EPSILON);
            body.setPos(safePos);
            body.setVector(new Vector(body.getVector().getX(), 0));

            if (i.normal.getY() > 0 && body.getEntity() instanceof Worm) {
                ((Worm) body.getEntity()).setOnGround(true);
                world.getBus().postQueued(new GroundCheckCollision(body.getEntity()));
            }
        } else {
            body.setPos(nextPos);
        }
    }

    /**
     * <p>
     * Simulates the case when the {@link PhysicsBody} has to bounce at it first collision
     * using the formula reflected_speed = init_speed - 2 * (init_speed * normal) * normal
     * </p>
     * @param body the {@link PhysicsBody} which move
     * @param impact the {@link Impact} with collision's information
     * @param restitution the coefficient of restitution (energy loss, between 0 and 1).
     */
    public void bounce(PhysicsBody body, Impact impact, double restitution) {
        Position safePos = new Position(
                impact.collisionPos.getX() + impact.normal.getX() * EPSILON,
                impact.collisionPos.getY() + impact.normal.getY() * EPSILON
        );
        body.setPos(safePos);
        // normal vector
        Vector normal = impact.normal;
        // init_speed vector
        Vector velocity = body.getVector();
        // Calculation of the scalar product of init_vector and normal
        double dot = velocity.getX() * normal.getX() + velocity.getY() * normal.getY();
        // End of reflected_speed calculation
        Vector reflectedVelocity = velocity.sub(normal.scale(2 * dot));
        body.setVector(reflectedVelocity.scale(restitution));
    }

    /**
     * Simulates the case when the {@link PhysicsBody} has to stay where it has it first collision
     * @param body the {@link PhysicsBody} which move
     * @param impact the {@link Impact} with collision's information
     */
    public void touch(PhysicsBody body, Impact impact) {
        // we don't add EPSILON if the normal is upwards to avoid to have a gap between body and ground
        if (impact.normal.getY() > 0) {
            body.setPos(impact.collisionPos);
        } else {
            Position safePos = new Position(
                    impact.collisionPos.getX() + impact.normal.getX() * EPSILON,
                    impact.collisionPos.getY() + impact.normal.getY() * EPSILON
            );
            body.setPos(safePos);
        }
        // body's vector set to (0, 0)
        body.setVector(new Vector(0, 0));
    }

    /**
     * Simulates the case when the {@link PhysicsBody} cross map and other {@link PhysicsBody}
     * @param body the {@link PhysicsBody} which move
     * @param pos the {@link Position} where the {@link PhysicsBody} will be after the tick
     */
    public void cross(PhysicsBody body, Position pos) {
        body.setPos(pos);
    }

    /**
     * Getter for the map
     * @return the {@link Map} of the collision engine
     */
    public Map getMap() {
        return this.map;
    }

    /**
     * <p>
     * Checks if a physics body is in damaging terrain (lava, traps) by testing all tiles
     * that intersect with the body's bounding box.
     * </p>
     * <p>
     * The method uses the body's position, width, and height to determine which tiles
     * to test, covering the entire area the body occupies.
     * </p>
     * @param body the {@link PhysicsBody} to check
     * @return the {@link Tile} if the body is in damaging terrain, {@code null} otherwise
     */
    public Tile inTrapOrLava(PhysicsBody body) {
        Position pos = body.getPos();

        // Calculate the bounding box of the body
        int minX = (int) Math.floor(pos.getX());
        int maxX = (int) Math.ceil(pos.getX() + body.getWidth());
        int minY = (int) Math.floor(pos.getY());
        int maxY = (int) Math.ceil(pos.getY() + body.getHeight());

        // Test all tiles in the bounding box
        for (int tx = minX; tx <= maxX; tx++) {
            for (int ty = minY; ty <= maxY; ty++) {
                Position tilePos = new Position(tx, ty);
                if (map.isDamaging(tilePos)) {
                    return map.getTile(tilePos);
                }
            }
        }

        return null;
    }
}