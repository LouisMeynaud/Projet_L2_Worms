package com.lllmtb.model.entity.team;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.inventory.Inventory;
import com.lllmtb.model.item.weapon.FireParams;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.model.item.weapon.WeaponRegistry;
import com.lllmtb.physics.Position;

public class Team{
    private final Map<Integer, Worm> worms;
    private final Inventory inventory;
    public final String name;
    private int numberOfPlayers;
    public final int id;
    private static int ids;
    private Worm currentWorm;


    /*Private constructor to initialize Team
     * @param name : Name of the team, @param numberOfPlayers : Number of Players in the Team, @param worms : Set of worm in the team
     * @param inventory : inventory of the team, @param playersName : Array with name of all players
     */
    private Team(String name,int numberOfPlayers, Map<Integer,Worm> worms, Inventory inventory, Position spawn){
        this.worms = worms;
        this.inventory = inventory;
        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
        this.id = ++Team.ids;
        this.setUpPlayers(numberOfPlayers, spawn);
    }

    /*Public constructor to initialize Team */
    public Team(String teamName, int numberOfPlayers, Position spawn){
        this(teamName,numberOfPlayers, new HashMap<>(), new Inventory(), spawn);
    }

    //Getter
    public Inventory getInventory(){
        return this.inventory;
    }

    public Map<Integer, Worm> getWorms(){
        return this.worms;
    }
    public int getNumberOfPlayers(){
        return this.numberOfPlayers;
    }


    /* Create all worm in the team
     * @param numberOfPlayers : number of player @param playersName : name of all players
     */
    private void setUpPlayers(int numberOfPlayers, Position spawn){
        for(int i = 1; i <= numberOfPlayers ; i++ ){
            this.addPlayer(new Worm(this.name+i, this, spawn));
        }
    }


    public void addPlayer(Worm worm){
        this.worms.put(worm.uuid, worm);
    }

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




    


    




}
