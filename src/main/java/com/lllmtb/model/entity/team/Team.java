package com.lllmtb.model.entity.team;

import java.util.*;


import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.inventory.Inventory;
import com.lllmtb.model.item.weapon.FireParams;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.Position;

public class Team{
    private final ArrayList<Worm> worms = new ArrayList<Worm>();
    private final Inventory inventory;
    public final String name;
    private int numberOfPlayers;
    public final int id;
    private static int ids;
    private Worm currentWorm;
    private int index = 0;
    public final TeamColor teamColor;
    private static List<TeamColor> teamColorPossible = Arrays.asList(TeamColor.values());


    /*Private constructor to initialize Team
     * @param name : Name of the team, @param numberOfPlayers : Number of Players in the Team, @param worms : Set of worm in the team
     * @param inventory : inventory of the team, @param playersName : Array with name of all players
     */
    private Team(String name,int numberOfPlayers, Inventory inventory, Position spawn){
        this.inventory = inventory;
        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
        this.id = ++Team.ids;
        this.setUpPlayers(numberOfPlayers, spawn);
        this.currentWorm = this.worms.get(0);
        this.teamColor = Team.chooseColor();
    }

    /*Public constructor to initialize Team */
    public Team(String teamName, int numberOfPlayers, Position spawn){
        this(teamName,numberOfPlayers, new Inventory(), spawn);
    }

    //Getter
    public Inventory getInventory(){
        return this.inventory;
    }

    public ArrayList<Worm> getTeamWorms(){
        return this.worms;
    }
    public int getNumberOfPlayers(){
        return this.numberOfPlayers;
    }
    public Worm getIndexWorm(int index){
        return this.worms.get(index);
    }
    public Worm getCurrentWorm(){
        return this.currentWorm;
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
        this.worms.add(worm);
        this.numberOfPlayers = this.worms.size();
    }

    public void removePlayer(int index){
        this.worms.remove(index);
        this.numberOfPlayers = this.worms.size();
    }

    //Choose a color from the enum for the team
    public static TeamColor chooseColor(){
        Random random = new Random();
        return Team.teamColorPossible.get(random.nextInt(Team.teamColorPossible.size()));
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
        this.index = (++this.index >= this.worms.size()) ? 0 : this.index;
        this.currentWorm = this.getIndexWorm(this.index);
    }




    


    




}
