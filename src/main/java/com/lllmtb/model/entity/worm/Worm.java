package com.lllmtb.model.entity.worm;

import com.lllmtb.model.entity.Entity;
import com.lllmtb.physics.PhysicsBody;

public class Worm extends Entity {

    // Champs
    private Team team;
    private String name; 
    private Items selfItem = null;
    private Statistics stats;
    private boolean alive = true;
    private int width = 2;
    private int height = 2;

    //Constructor
    public Worm(PhysicsBody body, String name, Statistics stats, Team team){
        super(body);
        this.name = name;
        this.stats = stats;
        this.team = team;
    }

    // Constructeur Worm solitaire 
    public Worm(PhysicsBody body, String name, Statistics stats){
        super(body);
        this.name = name;
        this.stats = stats;
    }

    //Methods
     // Bloc Weapon, shoot

    public void shoot(){}; // to shoot with a weapon --> Weapon, 


    // to select a item from the inventory's team from a slot
    public void selectItems(int slot){ 
        this.team.selectItems(slot);
    }; 


    
    // Bloc useful function 

    
    /*
     * Damage a worm with current self item 
     * @param target : Worm targeted
     */
    public void damageToWorm(Worm target){
        this.stats.setHp(this.selfItem.getDamage()); 
    }

    /*
     * Add Ammo in the inventory's team
     * @param weapon : weapon to which ammo must be added
     */
    public void addAmmo(Weapon weapon){
        this.team.addAmmo(weapon);
    }
     
    /*
     * Unlock a weeapon for the worm's team
     * @param weapon : weapon which be unlocked
     */
    public void unlockWeapon(Weapon weapon){
        this.team.unlockWeapon(weapon);
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
