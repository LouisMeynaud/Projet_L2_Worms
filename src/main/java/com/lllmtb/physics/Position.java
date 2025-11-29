package com.lllmtb.physics;

import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

/**
 * The {@link Position} class represents a two-dimensional point in space
 * using Cartesian coordinates {@code (x, y)}.
 */
public class Position {
    private double x;
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

    /** Getters */
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }

    /**
     * @param other another {@link Position}.
     * @return the {@link Vector} from other to this position.
     */
    public Vector sub(@NonNull Position other) {
        Objects.requireNonNull(other);
        return new Vector(this.x - other.x, this.y - other.y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    public boolean equals(Position other){
        if (other == null) return false;
        return this.getX() == other.getX() && this.getY() == other.getY();
    }
}
