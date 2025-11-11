package com.lllmtb.model.entity.worm;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.Position;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.Vector;
import org.eclipse.jdt.annotation.NonNull;

import java.util.Objects;

public class Worm extends Entity {
    private final PhysicsBody body;

    // Champs
    private Team team;
    public final String name; 
    private WeaponId selfItem = null;
    private final Statistics stats;
    private boolean alive = true;

    //Constructor
    private Worm(String name, Statistics stats, Team team, @NonNull Position spawn){
        super();
        Objects.requireNonNull(spawn);
        this.body = new PhysicsBody(this, spawn, new Vector(0, 0), 100, 1, 1);
        this.name = name;
        this.stats = stats;
        this.team = team;
        this.spawn(spawn);

    }

    public Worm(String name, Team team, Position spawn){
        this(name, new Statistics(100, 1, 1), team, spawn);
    }

    // Getter 
    public WeaponId getSelfItem(){
        return this.selfItem;
    }
    public Statistics getStats(){
        return this.stats;
    }
    public Team getTeam(){
        return this.team;
    }
    public PhysicsBody getBody(){
        return this.body;
    }

    //Setter
    public void setSelfItem(WeaponId id){
            this.selfItem = id;
    }

    //Methods
    //Function damage to worm

    // Bloc initialisation and set for worm
    //Spawn a worm on a specific bloc
    public void spawn(Position spawn){
        this.body.setPos(spawn);
    }

    /* Returns the status of Worm
     * @return a boolean, true for alive, false for death.
     */
    public boolean isAlive(){return this.alive;}

    // Set the worm's Death
    public void setDeath(){
        if(!this.stats.isAlive()){
            this.alive = false;
        }
    }
}
