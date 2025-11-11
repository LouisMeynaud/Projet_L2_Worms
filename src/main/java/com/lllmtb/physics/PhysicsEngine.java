package com.lllmtb.physics;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Allows storage of a collection of PhysicsBody objects (those associated with Entity objects that currently exist).
 * Allows management of this collection, including updating it.
 * This class is used to manage the movements/trajectories of Entity objects associated with PhysicsBody objects in the collection, taking collisions into account.
 */

public class PhysicsEngine {
    private ArrayList<PhysicsBody> bodies;
    private Vector gravity;
    //private boolean isWind;
    //private Vector wind;
    public static double ticktime = 0.0167; //Value to be determined

    
    /**
     * Creates a physics engine instance with gravity initialized at (0, -9.81).
     * @param isWind A boolean indicating if there's wind.
     */ 

    public PhysicsEngine(boolean isWind){
        this.bodies = new ArrayList<>();
        this.gravity = new Vector(0, - 9.81);
        //this.isWind = isWind;
        //this.wind = new Vector(0, 0);
    }

    /**
     * Returns the list of all physics bodies currently managed by the physics engine.
     * @return an {@link ArrayList} containing all {@link PhysicsBody} objects.
     */
    public ArrayList<PhysicsBody> getBodies() {
        return bodies;
    }

    /**
     * Returns the gravity vector applied to all bodies in the simulation.
     * @return the {@link Vector} representing the gravity.
     */
    public Vector getGravity() {
        return gravity;
    }

    /**
     * Returns the duration of one simulation tick in seconds.
     * This value determines how often the physics engine updates the state of the system.
     * @return the tick time as a {@code double}.
     */
    public static double getTicktime() {
        return ticktime;
    }

    /*public boolean isWind() {
        return isWind;
    }
    public Vector getWind() {
        return wind;
    }*/
    

    /**
     * Adds a PhysicsBody to the collection.
     * @param body The PhysicsBody to add (if null, the PhysicsBody collection remains unchanged).
     */
    public void add(PhysicsBody body){
        Objects.requireNonNull(body);
        bodies.add(body);
    }

    /**
     * Removes a PhysicsBody from the collection.
     * @param body The PhysicsBody to be removed (if not present, the PhysicsBody collection remains unchanged).
     */
    public void remove(PhysicsBody body){
        bodies.remove(body);
    }

    /**
     * Removes all PhysicsBodies from the collection.
     */
    public void clear(){
        bodies.clear();
    }

    /**
     * Updates the state of all PhysicsBodies in the collection by applying physical laws (motion, forces, collisions, and so on.)
     * and triggering effects (explosions, and so on.)
     * This method is called every tick of the game.
     */
    public void next(){
       for(PhysicsBody body : bodies){
            Vector acc = body.getVector().copy();
            acc.scale(-0.2);
            acc.add(this.gravity);

            /*if(this.isWind){
                Vector random = new Vector(Math.random() / 3, Math.random() / 3);
                if(Math.random() < 0.5) this.wind.add(random);
                else this.wind.sub(random);
                sumForces.add(this.wind);
            }*/

            acc.scale(ticktime/body.getMass());
            Vector newVector = body.getVector().copy();
            newVector.add(acc);
            Position nextPos = newVector.newPos(body.getPos(), ticktime);
            body.setVector(newVector);
            if(!haveCollisionMap(body,nextPos)){
                body.setPos(nextPos);
            }
       }
    }
    
}

