package com.lllmtb.model.entity.team;

import java.util.Collection;
import java.util.HashSet;

import com.lllmtb.model.entity.worm.Worm;

public class Team{
    private Collection<Worm> worms;
    private Inventory inventory;
    public final String name;
    public final int id;
    private static int ids;
    private Worm currentWorm;

    /*Private constructor to initialize Team
     * @param name : Name of the team, @param numberofPlayers : Number of Players in the Team, @param worms : Set of worm in the team
     * @param inventory : inventory of the team, @param playersName : Array with name of all players
     */
    private Team(String name,int numberOfPlayers, Collection<Worm> worms, Inventory inventory, String[] playersName){
        this.worms = worms;
        this.inventory = inventory;
        this.name = name;
        this.id = Team.ids++;
        this.setUpPlayers(numberOfPlayers, playersName);
    }

    /*Public constructor to initialize Team */
    public Team(String teamName, int numberOfPlayers, String... playersName){
        this(teamName,numberOfPlayers, new HashSet<>(), new Inventory(), playersName);
    }

    /* Create all worm in the team
     * @param numberOfPlayers : number of player @param playersName : name of all players
     */
    private void setUpPlayers(int numberOfPlayers, String[] playersName){
        for(String name : playersName ){
            this.worms.add(new Worm(name, this));
        }   
    }


    public void addPlayer(Worm worm){
        this.worms.add(worm);
    }

    //Replace Argument worm with the uuid in the future ?
    public void removePlayer(Worm worm){
        this.worms.remove(worm);
    }

    




}
