package com.lllmtb.physics;

import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.terrain.map.Map;
import com.lllmtb.model.entity.Entity;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Central collision engine for the game, inspired by JBump principles (swept collision detection).
 * Handles collision detection for {@link Entity} and {@link Map} (Circle-AABB) and
 * between {@link Entity} (Circle-Circle).
 */
public class Collision {

    private static Map map;
    private static CopyOnWriteArrayList<PhysicsBody> bodies;

    /**
     * As in JBump, the class Impact allows to stack information from the collision test.
     */
    public static class Impact {
        public final PhysicsBody hitBody; // The hit body (null if it's the map)
        public final Position collisionPos; // The exact position of the collision (= finalPos if no collision)
        public final double ti; // Time of impact (0.0 if imediately, 1.0 if no collision)
        public final Vector normal; // The normal vector (perpendicular) to the hit surface
        public final boolean collided; // true if a collision is confirmed

        public Impact(Position pos) {
            this.hitBody = null;
            this.collisionPos = pos;
            this.ti = 1.0;
            this.normal = new Vector(0, 0);
            this.collided = false;
        }

        public Impact(double ti, Position finalPos, Vector normal, PhysicsBody hitBody) {
            this.hitBody = hitBody;
            this.collisionPos = finalPos;
            this.ti = ti;
            this.normal = normal;
            this.collided = true;
        }
    }

    public static void init(Map m, CopyOnWriteArrayList<PhysicsBody> b) {
        map = m;
        bodies = b;
    }

    public static Impact move(PhysicsBody body, Position endPos) {

        Impact finalImpact = new Impact(endPos);

        Impact mapImpact = testSweptCircleTile(body, endPos);
        if (mapImpact.collided && mapImpact.ti < finalImpact.ti) {
            finalImpact = mapImpact;
        }

        for (PhysicsBody other : bodies) {
            if (other != body) {
                if (!(body.getEntity() instanceof Worm && other.getEntity() instanceof Worm)) {
                    Impact bodyImpact = testSweptCircleCircle(body, other, endPos);
                    if (bodyImpact.collided && bodyImpact.ti < finalImpact.ti) {
                        finalImpact = bodyImpact;
                    }
                }
            }
        }

        return finalImpact;
    }

    private static Impact testSweptCircleTile(PhysicsBody body, Position endPos) {
        Position startPos = body.getPos();
        Vector displacement = new Vector(endPos.getX() - startPos.getX(), endPos.getY() - startPos.getY());
        double radius = body.getRadius();

        Impact nearestImpact = new Impact(endPos);

        int minX = (int) Math.floor(Math.min(startPos.getX(), endPos.getX()) - radius);
        int maxX = (int) Math.ceil(Math.max(startPos.getX(), endPos.getX()) + radius);
        int minY = (int) Math.floor(Math.min(startPos.getY(), endPos.getY()) - radius);
        int maxY = (int) Math.ceil(Math.max(startPos.getY(), endPos.getY()) + radius);

        for (int tx = minX; tx <= maxX; tx++) {
            for (int ty = minY; ty <= maxY; ty++) {
                Position tilePos = new Position(tx, ty);

                if (map.inMap(tilePos) && map.isSolid(tilePos)) {

                    if (isColliding(startPos, radius, tilePos)) {
                        Vector normal = calculateTileNormal(startPos, tilePos, radius);
                        return new Impact(0.0, startPos, normal, null);
                    }

                    double[] result = sweptCircleAABB(startPos, radius, displacement, tilePos);
                    double ti = result[0];
                    Vector normal = new Vector(result[1], result[2]);

                    if (ti >= 0.0 && ti < 1.0 && ti < nearestImpact.ti) {
                        Position hitPos = body.getVector().newPos(startPos, ti);
                        nearestImpact = new Impact(ti, hitPos, normal, null);
                    }
                }
            }
        }

        if (!map.inMap(endPos)) {
            return new Impact(0.0, startPos, new Vector(0, 0), null);
        }

        return nearestImpact;
    }

    private static Impact testSweptCircleCircle(PhysicsBody body1, PhysicsBody body2, Position endPos) {
        Position startPos1 = body1.getPos();
        double radius1 = body1.getRadius();
        double radius2 = body2.getRadius();
        Vector displacement = new Vector(endPos.getX() - startPos1.getX(), endPos.getY() - startPos1.getY());

        Position startPos2 = body2.getPos();
        double combinedRadius = radius1 + radius2;

        double ti = sweptPointCircle(startPos1, displacement, startPos2, combinedRadius);

        if (ti >= 0.0 && ti < 1.0) {
            Position hitPos = body1.getVector().newPos(startPos1, ti);

            Vector normalVec = hitPos.sub(body2.getPos());
            double length = Math.sqrt(normalVec.getX() * normalVec.getX() + normalVec.getY() * normalVec.getY());
            Vector normal = normalVec.scale(1.0 / length);

            return new Impact(ti, hitPos, normal, body2);
        }

        return new Impact(endPos);
    }

    private static boolean isColliding(Position circleCenter, double radius, Position tilePos) {
        double cx = circleCenter.getX();
        double cy = circleCenter.getY();
        double ax = tilePos.getX();
        double ay = tilePos.getY();

        double closestX = Math.max(ax, Math.min(cx, ax + 1.0));
        double closestY = Math.max(ay, Math.min(cy, ay + 1.0));

        double distanceX = cx - closestX;
        double distanceY = cy - closestY;
        double distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);

        return distanceSquared < (radius * radius);
    }

    private static Vector calculateTileNormal(Position hitPos, Position tilePos, double radius) {
        double cx = hitPos.getX();
        double cy = hitPos.getY();
        double tx = tilePos.getX();
        double ty = tilePos.getY();

        double closestX = Math.max(tx, Math.min(cx, tx + 1.0));
        double closestY = Math.max(ty, Math.min(cy, ty + 1.0));

        Vector normalVec = new Vector(cx - closestX, cy - closestY);

        double length = Math.sqrt(normalVec.getX() * normalVec.getX() + normalVec.getY() * normalVec.getY());
        if (length == 0.0) return new Vector(0, 0);

        return normalVec.scale(1.0 / length);
    }

    private static double[] sweptCircleAABB(Position startPos, double radius, Vector displacement, Position tilePos) {
        double tileX = tilePos.getX() - radius;
        double tileY = tilePos.getY() - radius;
        double tileW = 1.0 + 2 * radius;
        double tileH = 1.0 + 2 * radius;

        double dx = displacement.getX();
        double dy = displacement.getY();
        double x = startPos.getX();
        double y = startPos.getY();

        double txNearX = (tileX - x) / dx;
        double txFarX = (tileX + tileW - x) / dx;

        double tyNearY = (tileY - y) / dy;
        double tyFarY = (tileY + tileH - y) / dy;

        if (dx < 0) {
            double temp = txNearX; txNearX = txFarX; txFarX = temp;
        }
        if (dy < 0) {
            double temp = tyNearY; tyNearY = tyFarY; tyFarY = temp;
        }

        double tiNear = Math.max(txNearX, tyNearY);
        double tiFar = Math.min(txFarX, tyFarY);

        if (tiNear >= 1.0 || tiNear < 0.0 || tiNear >= tiFar) {
            return new double[]{1.0, 0, 0};
        }

        double normalX = 0;
        double normalY = 0;
        if (txNearX > tyNearY) {
            normalX = (dx > 0) ? -1 : 1;
        } else {
            normalY = (dy > 0) ? -1 : 1;
        }

        return new double[]{tiNear, normalX, normalY};
    }

    private static double sweptPointCircle(Position p1, Vector v1, Position c2, double r2) {

        Vector p1ToC2 = c2.sub(p1);

        double a = v1.getX() * v1.getX() + v1.getY() * v1.getY();
        double b = 2.0 * (v1.getX() * p1ToC2.getX() + v1.getY() * p1ToC2.getY());
        double c = p1ToC2.getX() * p1ToC2.getX() + p1ToC2.getY() * p1ToC2.getY() - r2 * r2;

        if (c < 0) return 0.0;

        if (a == 0.0) return 1.0;

        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0.0) return 1.0;

        double ti = (-b - Math.sqrt(discriminant)) / (2.0 * a);

        if (ti >= 0.0 && ti < 1.0) {
            return ti;
        }

        return 1.0;
    }

    public static void slide(PhysicsBody body, Impact impact) {
        body.setPos(impact.collisionPos);

        Vector normal = impact.normal;
        Vector velocity = body.getVector();

        double dot = velocity.getX() * normal.getX() + velocity.getY() * normal.getY();
        Vector remainingVelocity = velocity.sub(normal.scale(dot));

        body.setVector(remainingVelocity);
    }

    public static void bounce(PhysicsBody body, Impact impact, double restitution) {
        body.setPos(impact.collisionPos);

        Vector normal = impact.normal;
        Vector velocity = body.getVector();

        double dot = velocity.getX() * normal.getX() + velocity.getY() * normal.getY();

        Vector reflectedVelocity = velocity.sub(normal.scale(2 * dot));

        body.setVector(reflectedVelocity.scale(restitution));
    }

    public static void touch(PhysicsBody body, Impact impact) {
        body.setPos(impact.collisionPos);

        body.setVector(new Vector(0, 0));
    }

    public static void cross(PhysicsBody body, Impact impact) {}
}