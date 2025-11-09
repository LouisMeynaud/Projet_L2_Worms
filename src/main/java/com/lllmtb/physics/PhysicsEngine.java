package com.lllmtb.physics;

/**
 * Allows storage of a collection of PhysicsBody objects (those associated with Entity objects that currently exist).
 * Allows management of this collection, including updating it.
 * An implementation of this interface will be used to manage the movements/trajectories of Entity objects associated with the PhysicsBody objects in the collection,
 * as well as to trigger the effects caused by a collision with Entity objects associated with the PhysicsBody objects in the collection.
 */

public interface PhysicsEngine {

    /**
     * Adds a PhysicsBody to the collection.
     * @param body The PhysicsBody to add (if null, the PhysicsBody collection remains unchanged)
     */
    void add(PhysicsBody body);

    /**
     * Removes a PhysicsBody from the collection.
     * @param body The PhysicsBody to be removed (if not present, the PhysicsBody collection remains unchanged).
     */
    void remove(PhysicsBody body);

    /**
     * Removes all PhysicsBodies from the collection.
     */
    void clear();

    /**
     * Updates the state of all PhysicsBodies in the collection by applying physical laws (motion, forces, collisions, and so on.)
     * and triggering effects (explosions, and so on.)
     * This method is called every tick of the game.
     */
    void next();
}
