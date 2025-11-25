package com.lllmtb.physics;

import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.model.entity.Entity;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The {@code Collision} class is the central collision engine for the game, inspired by JBump principles (swept collision detection).
 * Handles collision detection for {@link Entity} and {@link Map} (Circle-AABB) and
 * between {@link Entity} (Circle-Circle).
 */
public class Collision {

    private final Map map;
    private final CopyOnWriteArrayList<PhysicsBody> bodies;

    /**
     * Constructor of Collision, with the map and the entities to check
     * @param m the {@link Map} of the game
     * @param b the {@link CopyOnWriteArrayList} containing active {@link Entity}'s {@link PhysicsBody}.
     */
    public Collision(Map m, CopyOnWriteArrayList<PhysicsBody> b) {
        this.map = m;
        this.bodies = b;
    }

    /**
     * As in JBump, the {@code Impact} class allows to stack information from the collision test.
     */
    public static class Impact {
        public final PhysicsBody hitBody; // The hit body (null if it's the map)
        public final Position collisionPos; // The exact position of the collision (= finalPos if no collision)
        public final double ti; // Time of impact (0.0 if immediately, 1.0 if no collision)
        public final Vector normal; // The normal vector (perpendicular) to the hit surface
        public final boolean collided; // true if a collision is confirmed

        /**
         * Constructor of Impact when there isn't any collision
         *
         * @param pos the {@link Position} that is the final position
         */
        public Impact(Position pos) {
            this.hitBody = null;
            this.collisionPos = pos;
            this.ti = 1.0;
            this.normal = new Vector(0, 0);
            this.collided = false;
        }

        /**
         * Constructor of impact when there is a collision
         *
         * @param ti      the {@code double} which represents the time of impact, between {@code 0.0} and {@code 1.0}.
         * @param body     the {@link PhysicsBody} of the entity in movement.
         * @param normal  the normal {@link Vector} of the hit structure.
         * @param hitBody the {@link PhysicsBody} of the collided entity ({@code null} if it's the map).
         */
        public Impact(double ti, PhysicsBody body, Vector normal, PhysicsBody hitBody) {
            this.hitBody = hitBody;
            this.collisionPos = body.getVector().newPos(body.getPos(), ti);;
            this.ti = ti;
            this.normal = normal;
            this.collided = true;
        }
    }

    /**
     * <p>
     * Simulates the displacement of a {@link PhysicsBody} based on a vector to determine
     * when and where will be the first collision if there is one
     * Worms are rectangles with a ratio of height 2 / width 1 and others bodies are squares.
     * So, to test the collision with a worm, we make two circles and we test them.
     * </p>
     * @param body the {@link PhysicsBody} in movement
     * @param endPos the final {@link Position} of {@code body} without collision.
     * @return an {@link Impact} with the information about possible collisions
     */
    public Impact move(PhysicsBody body, Position endPos) {
        Impact finalImpact = new Impact(endPos);

        // if the body is a worm
        if (body.getEntity() instanceof Worm) {
            // Calculation of the two circles radius
            double radius = body.getWidth()/2;
            Position wormHead = new Position(body.getPos().getX(),body.getPos().getY() + 1.0);
            Position endPos2 = new Position(endPos.getX(), endPos.getY() + 1.0);
            // test collision for the two circles of the worm
            Impact mapImpact1 = this.testSweptCircleTile(body, body.getPos(), radius, endPos);
            Impact mapImpact2 = this.testSweptCircleTile(body, wormHead, radius, endPos2);
            if (mapImpact1.ti < finalImpact.ti) {
                finalImpact = mapImpact1;
            }
            if (mapImpact2.ti < finalImpact.ti) {
                finalImpact = mapImpact2;
            }
            // test with other bodies on the map without other worms
            for (PhysicsBody other : this.bodies) {
                if ((body != other) && !(other.getEntity() instanceof Worm)) {
                    double radiusOther = other.getHeight()/2;
                    Position otherPos = new Position(other.getPos().getX() + 0.5, other.getPos().getY() + 0.5);
                    Impact bodyImpact = this.testSweptCircleCircle(body, body.getPos(), radius, other, otherPos, radiusOther, endPos);
                    if (bodyImpact.ti < finalImpact.ti) {
                        finalImpact = bodyImpact;
                    }
                    Impact bodyImpact2 = this.testSweptCircleCircle(body, wormHead, radius, other, otherPos, radiusOther, endPos2);
                    if (bodyImpact2.ti < finalImpact.ti) {
                        finalImpact = bodyImpact2;
                    }
                }
            }
        } else {
            // if the body is not a worm
            double radius = body.getHeight()/2;
            // Entity-Map Test: searches for the earliest impact time
            Impact mapImpact = this.testSweptCircleTile(body, body.getPos(), radius, endPos);
            if (mapImpact.collided && mapImpact.ti < finalImpact.ti) {
                finalImpact = mapImpact;
            }

            // Entity-Entity Test: iterates over all other bodies to find the earliest impact time
            for (PhysicsBody other : this.bodies) {
                if (other != body) {
                    double radiusOther = other.getWidth()/2;
                    Position otherPos = new Position(other.getPos().getX() + 0.5, other.getPos().getY() + 0.5);
                    Impact bodyImpact = this.testSweptCircleCircle(body, body.getPos(), radius, other, otherPos, radiusOther, endPos);
                    if (bodyImpact.ti < finalImpact.ti) {
                        finalImpact = bodyImpact;
                    }
                    if (other.getEntity() instanceof Worm) {
                        Position wormHead = new Position(other.getPos().getX() + 0.5, other.getPos().getY() + 1.5);
                        bodyImpact = this.testSweptCircleCircle(body, body.getPos(), radius, other, wormHead, radiusOther, endPos);
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
     * @return The nearest impact with a solid tile.
     */
    private Impact testSweptCircleTile(PhysicsBody body, Position startPos, double radius, Position endPos) {
        Position startPos2 = new Position(startPos.getX() + radius, startPos.getY() + radius);
        Vector displacement = body.getVector();

        Impact nearestImpact = new Impact(endPos);

        // Determine the expanded AABB area on the grid to check
        int minX = (int) Math.floor(Math.min(startPos.getX(), endPos.getX()));
        int maxX = (int) Math.ceil(Math.max(startPos.getX(), endPos.getX()));
        int minY = (int) Math.floor(Math.min(startPos.getY(), endPos.getY()));
        int maxY = (int) Math.ceil(Math.max(startPos.getY(), endPos.getY()));

        for (int tx = minX; tx <= maxX; tx++) {
            for (int ty = minY; ty <= maxY; ty++) {
                Position tilePos = new Position(tx, ty);

                if (this.map.inMap(tilePos)) {

                    if (this.map.isSolid(tilePos)) {

                        // Immediate overlap test (ti=0.0)
                        if (isColliding(startPos2, radius, tilePos)) {
                            Vector normal = this.calculateTileNormal(startPos2, tilePos);
                            return new Impact(0.0, body, normal, null);
                        }

                        // Swept AABB test
                        double[] result = this.sweptCircleAABB(startPos2, radius, displacement, tilePos);
                        double ti = result[0];
                        Vector normal = new Vector(result[1], result[2]);

                        if (ti >= 0.0 && ti < 1.0 && ti < nearestImpact.ti) {
                            nearestImpact = new Impact(ti, body, normal, null);
                        }
                    }
                } else {
                    // Immediate overlap test (ti=0.0)
                    if (isColliding(startPos2, radius, tilePos)) {
                        Vector normal = this.calculateTileNormal(startPos2, tilePos);
                        return new Impact(0.0, body, normal, null);
                    }

                    // Swept AABB test
                    double[] result = this.sweptCircleAABB(startPos2, radius, displacement, tilePos);
                    double ti = result[0];
                    Vector normal = new Vector(result[1], result[2]);

                    if (ti >= 0.0 && ti < 1.0 && ti < nearestImpact.ti) {
                        nearestImpact = new Impact(ti, body, normal, null);
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
     * @return The nearest impact with body2.
     */
    private Impact testSweptCircleCircle(PhysicsBody body1, Position startPos1, double radius1,PhysicsBody body2, Position startPos2, double radius2, Position endPos) {
        Position startPos = new Position(startPos1.getX() + radius1, startPos1.getY() + radius1);
        Vector displacement = new Vector(endPos.getX() - startPos1.getX(), endPos.getY() - startPos1.getY());

        double combinedRadius = radius1 + radius2;

        // Use quadratic formula resolution to find ti
        double ti = sweptPointCircle(startPos, displacement, startPos2, combinedRadius);

        if (ti >= 0.0 && ti < 1.0) {
            // Calculate the exact impact position
            Position hitPos = body1.getVector().newPos(startPos1, ti);

            // Calculate the normal vector: from body2's center to body1's center
            Vector normalVec = hitPos.sub(body2.getPos());
            double length = Math.sqrt(normalVec.getX() * normalVec.getX() + normalVec.getY() * normalVec.getY());
            Vector normal = normalVec.scale(1.0 / length);

            return new Impact(ti, body1, normal, body2);
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
        if (length == 0.0) return new Vector(0, 0);

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
            double temp = txNearX; txNearX = txFarX; txFarX = temp;
        }
        if (dy < 0) {
            double temp = tyNearY; tyNearY = tyFarY; tyFarY = temp;
        }

        // tiNear is the moment both X and Y conditions are met (take the max)
        double tiNear = Math.max(txNearX, tyNearY);
        // tiFar is the moment the body leaves the area (take the min)
        double tiFar = Math.min(txFarX, tyFarY);

        // Check impact validity
        if (tiNear >= 1.0 || tiNear < 0.0 || tiNear >= tiFar) {
            return new double[]{1.0, 0, 0};
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

        return new double[]{tiNear, normalX, normalY};
    }

    /**
     * <p>
     * Calculate the time of impact between two {@link Entity} by using the {@link Position}
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
        if (a == 0.0) return 1.0;
        // Calculation of the discriminant
        double discriminant = b * b - 4 * a * c;
        // if discriminant < 0, so no collision
        if (discriminant < 0.0) return 1.0;
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
     * using the formula slippery_speed = init_speed - perpendicular_speed with
     * perpendicular_speed = (init_speed * normal) * normal
     * </p>
     * @param body the {@link PhysicsBody} which move
     * @param impact the {@link Impact} with collision's information
     */
    public void slide(PhysicsBody body, Impact impact) {
        body.setPos(impact.collisionPos);
        Vector fallVector = new Vector(0, -1);
        body.setVector(fallVector);
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
        body.setPos(impact.collisionPos);
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
        body.setPos(impact.collisionPos);
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
}