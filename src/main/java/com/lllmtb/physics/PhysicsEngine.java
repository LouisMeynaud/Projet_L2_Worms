package com.lllmtb.physics;
import java.util.HashSet;
import java.util.Set;

/**
 * Allows storage of a collection of PhysicsBody objects (those associated with Entity objects that currently exist).
 * Allows management of this collection, including updating it.
 * This class is used to manage the movements/trajectories of Entity objects associated with the PhysicsBody objects in the collection,
 * as well as to trigger the effects caused by a collision with Entity objects associated with the PhysicsBody objects in the collection.
 */

public class PhysicsEngine {
    private Set<PhysicsBody> setb;
    private Vector gravity;
    private Vector wind;
    public static double ticktime = 0.0167; //Value to be determined


    public PhysicsEngine(){
        this.setb = new HashSet<>();
        this.gravity = new Vector(0, - 9.81);
        this.wind = new Vector(0, 0);
    }
    /**
     * Adds a PhysicsBody to the collection.
     * @param body The PhysicsBody to add (if null, the PhysicsBody collection remains unchanged)
     */
    public void add(PhysicsBody body){
        setb.add(body);
    }

    /**
     * Removes a PhysicsBody from the collection.
     * @param body The PhysicsBody to be removed (if not present, the PhysicsBody collection remains unchanged).
     */
    public void remove(PhysicsBody body){
        setb.remove(body);
    }

    /**
     * Removes all PhysicsBodies from the collection.
     */
    public void clear(){
        setb.clear();
    }

    /**
     * Updates the state of all PhysicsBodies in the collection by applying physical laws (motion, forces, collisions, and so on.)
     * and triggering effects (explosions, and so on.)
     * This method is called every tick of the game.
     */
    public void next(){
       for(PhysicsBody body : setb){

            Vector sumForces = new Vector();
           
            Vector airResistance = new Vector();
            airResistance = body.getVector();
            airResistance.scale(-0.2);

            sumForces.add(this.gravity);
            sumForces.add(this.wind);
            sumForces.add(airResistance);

            Vector a = new Vector(sumForces.getX(),sumForces.getY());
            a.scale(1/body.getMass());

            body.setVector(new Vector(body.getVector().getX() + a.getX() * ticktime, body.getVector().getY() + a.getY() * ticktime));
            body.setPos(new Position(body.getPos().getX() + body.getVector().getX() * ticktime, body.getPos().getY() + body.getVector().getY() * ticktime));


           
       }
    }
}

