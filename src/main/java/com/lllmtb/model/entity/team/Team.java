package com.lllmtb.model.entity.team;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.lllmtb.model.entity.worm.Worm;

public class Team{
    private Map<Integer, Worm> worms;
    private Inventory inventory;
    public final String name;
    public final int id;
    private static int ids;
    private Worm currentWorm;
    private int cursorWorm;

    /*Private constructor to initialize Team
     * @param name : Name of the team, @param numberofPlayers : Number of Players in the Team, @param worms : Set of worm in the team
     * @param inventory : inventory of the team, @param playersName : Array with name of all players
     */
    private Team(String name,int numberOfPlayers, Map<Integer,Worm> worms, Inventory inventory, String[] playersName){
        this.worms = worms;
        this.inventory = inventory;
        this.name = name;
        this.id = Team.ids++;
        this.setUpPlayers(numberOfPlayers, playersName);
    }

    /*Public constructor to initialize Team */
    public Team(String teamName, int numberOfPlayers, String... playersName){
        this(teamName,numberOfPlayers, new HashMap<>(), new Inventory(), playersName);
    }

    /* Create all worm in the team
     * @param numberOfPlayers : number of player @param playersName : name of all players
     */
    private void setUpPlayers(int numberOfPlayers, String[] playersName){
        for(String name : playersName ){
            this.addPlayer(new Worm(name, this));
        }
    }


    public void addPlayer(Worm worm){
        this.worms.put(worm.uuid, worm);
    }

    //Replace Argument worm with the uuid in the future ?
    public void removePlayer(int uuid){
        this.worms.remove(uuid);
    }

    // Inventory methods

    /*Unlock a weapon  and lock a weapon
     * @param id : to know what weapon to unlock or lock
    */
    public void unlockWeapon(WeaponId id) throws IllegalArgumentException{
        this.inventory.unlock(id);
    }

    public void lockWeapon(WeaponId id){
        this.inventory.lock(id);
    }
    /* Add Ammo in the inventory's team
     * @param id : weapon to which ammo must be added @param n : number of ammo
     */
    // Type enum
    public void addAmmo(WeaponId id, Integer n) throws IllegalArgumentException{
        this.inventory.addAmmo(id, n);
    }


    // Create FireParams in this method ?
    public Projectile fire(FireParams fireParams) throws IllegalArgumentException{
        return this.inventory.fire(this.currentWorm.getSelfItem(), fireParams);
    }

    // to select a item from the inventory's team from a slot
    public void selectItem(WeaponId id){
        this.currentWorm.setSelfItem(id);
    }


    //Worms method
    public void nextWorm(){
        
    }



    


    




}
