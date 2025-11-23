package com.lllmtb.physics;

/**
 * The {@code Position} class represents a two-dimensional point in space
 * using Cartesian coordinates {@code (x, y)}.
 * <p>
 * This class is typically used to describe the spatial location of a 
 * {@link PhysicsBody} or any entity in a physics simulation.
 * Instances of {@code Position} are immutable in the sense that their
 * fields are not modified directly after construction.
 * </p>
 */
public class Position {

    /** The x-coordinate of this position. */
    private double x;

    /** The y-coordinate of this position. */
    private double y;

    /**
     * Constructs a new {@code Position} with the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate of this position.
     *
     * @return the x-coordinate
     */
    public double getX() {
        return this.x;
    }

    /**
     * Returns the y-coordinate of this position.
     *
     * @return the y-coordinate
     */
    public double getY() {
        return this.y;
    }

    /**
     * @param other another {@link Position}.
     * @return the {@link Vector} from other to this position.
     */
    public Vector sub(Position other) {
        return new Vector(this.x - other.x, this.y - other.y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
