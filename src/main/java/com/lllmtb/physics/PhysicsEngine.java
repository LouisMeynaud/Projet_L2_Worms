package com.lllmtb.physics;
import java.util.ArrayList;


/**
 * Allows storage of a collection of PhysicsBody objects (those associated with Entity objects that currently exist).
 * Allows management of this collection, including updating it.
 * This class is used to manage the movements/trajectories of Entity objects associated with the PhysicsBody objects in the collection,
 * as well as to trigger the effects caused by a collision with Entity objects associated with the PhysicsBody objects in the collection.
 */

public class PhysicsEngine {
    private ArrayList<PhysicsBody> bodies;
    private Vector gravity;
    //private boolean isWind;
    //private Vector wind;
    public static double ticktime = 0.0167; //Value to be determined

    public PhysicsEngine(boolean isWind){
        this.bodies = new ArrayList<>();
        this.gravity = new Vector(0, - 9.81);
        //this.isWind = isWind;
        //this.wind = new Vector(0, 0);
    }

    public ArrayList<PhysicsBody> getBodies() {
        return bodies;
    }
    public Vector getGravity() {
        return gravity;
    }
    /*public boolean isWind() {
        return isWind;
    }
    public Vector getWind() {
        return wind;
    }*/
    public static double getTicktime() {
        return ticktime;
    }

    /**
     * Adds a PhysicsBody to the collection.
     * @param body The PhysicsBody to add (if null, the PhysicsBody collection remains unchanged)
     */
    public void add(PhysicsBody body){
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
    public void nextForOneBody(PhysicsBody body){
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
        body.setVector(newVector);
        body.setPos(body.getVector().newPos(body.getPos(), ticktime));
    }


    public void next(){
       for(PhysicsBody body : bodies){
            nextForOneBody(body);
       }
    }
    
}

