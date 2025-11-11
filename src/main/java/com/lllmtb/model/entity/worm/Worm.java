package com.lllmtb.model.entity.worm;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.model.entity.worm.Statistics;


public class Worm extends Entity {

    // Champs
    private Team team;
    public final String name; 
    private Item selfItem = null;
    private Statistics stats;
    private boolean alive = true;

    //Constructor
    private Worm(String name, Statistics stats, Team team){
        super(new PhysicsBody(new Position(0, 0), null, 20, 1, 1));
        this.name = name;
        this.stats = stats;
        this.team = team;

    }

    public Worm(String name, Team team){
        this(name, new Statistics(100, 1, 1), team);

    }

    //Methods
    // Bloc useful function 
    /*
     * Damage a worm with current self item 
     * @param target : Worm targeted
     */
    public void damageToWorm(Worm target){
        this.stats.setHp(this.selfItem.getDamage()); 
        if(!isAlive()){
            this.setDeath();
        }
    }
    // Bloc Intialisation et modificateurs des Worms
    
    //Spawn a worm on a specific bloc
    public void spawn(){}



    /*
     * Returns the status of Worm
     * @return a boolean, true for alive, false for death.
     */
    public boolean isAlive(){return this.alive;}
    

    /*
     * Set the worm's Death
     */
    public void setDeath(){
        if(!this.stats.isAlive()){
            this.alive = false;
        }
    }
    
}
