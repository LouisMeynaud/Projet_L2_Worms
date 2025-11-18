package com.lllmtb.model.entity.team;

import java.util.*;

import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.inventory.Inventory;
import com.lllmtb.model.item.weapon.FireParams;
import com.lllmtb.model.item.weapon.WeaponId;
import com.lllmtb.physics.Position;

/**
 * Represents a team of {@link Worm} entities in the game.
 * Each team has a name, color, inventory, and a collection of worms.
 * A team can manage its worms, inventory, and perform actions such as firing projectiles
 * and switching between active worms.
 *
 * <p>Each team is assigned a unique ID and color automatically upon creation.</p>
 *
 */
public class Team {

    /** List of worms belonging to the team. */
    private final ArrayList<Worm> worms = new ArrayList<>();

    /** Inventory containing weapons and items for the team. */
    private final Inventory inventory;

    /** The name of the team. */
    public final String name;

    /** Number of players (worms) in the team. */
    private int numberOfPlayers;

    /** Unique ID of the team. */
    public final int id;

    /** Static counter for assigning unique team IDs. */
    private static int ids;

    /** The currently active worm of the team. */
    private Worm currentWorm;

    /** Index of the currently active worm. */
    private int index = 0;

    /** The color associated with this team. */
    public final TeamColor teamColor;

    /** Possible colors that can be assigned to a team. */
    private static List<TeamColor> teamColorPossible = Arrays.asList(TeamColor.values());


    /**
     * Private constructor used to initialize a team with a given name, number of players, inventory, and spawn position.
     * @param name the name of the team
     * @param numberOfPlayers number of worms in the team
     * @param inventory the inventory assigned to the team
     * @param spawn the spawn position where the worms will be placed
     */
    private Team(String name, int numberOfPlayers, Inventory inventory, Position spawn) {
        this.inventory = inventory;
        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
        this.id = ++Team.ids;
        this.setUpPlayers(numberOfPlayers, spawn);
        this.currentWorm = this.worms.get(0);
        this.teamColor = Team.chooseColor();
    }

    /**
     * Public constructor used to initialize a team using the private constructor.
     * @param teamName the name of the team
     * @param numberOfPlayers number of worms in the team
     * @param spawn the spawn position where the worms will be placed
     */
    public Team(String teamName, int numberOfPlayers, Position spawn) {
        this(teamName, numberOfPlayers, new Inventory(), spawn);
    }

    /**
     * Returns the inventory associated with the team.
     * @return the team's inventory
     */
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Returns all worms belonging to the team.
     * @return list of worms
     */
    public ArrayList<Worm> getTeamWorms() {
        return this.worms;
    }

    /**
     * Returns the number of worms currently in the team.
     * @return number of worms
     */
    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    /**
     * Returns the worm at a specific index.
     * @param index index of the worm in the team
     * @return worm at the given index
     */
    public Worm getIndexWorm(int index) {
        return this.worms.get(index);
    }

    /**
     * Returns the currently active worm.
     * @return the current worm
     */
    public Worm getCurrentWorm() {
        return this.currentWorm;
    }

    /**
     * Creates and adds worms to the team based on the number of players.
     * @param numberOfPlayers number of worms to create
     * @param spawn spawn position for all worms
     */
    private void setUpPlayers(int numberOfPlayers, Position spawn) {
        for (int i = 1; i <= numberOfPlayers; i++) {
            new Worm(this.name + i, this);
            this.worms.get(i-1).spawn(spawn);
        }
    }

    /**
     * Adds a new worm to the team.
     * @param worm the worm to add
     */
    public void addPlayer(Worm worm) {
        this.worms.add(worm);
        this.numberOfPlayers = this.worms.size();
    }

    /**
     * Removes a worm from the team based on its index.
     * @param indexPlayer index of the worm to remove
     */
    public void removePlayer(int indexPlayer) {
        this.worms.remove(indexPlayer-1);
        this.numberOfPlayers = this.worms.size();
    }

    /**
     * Randomly selects a color from the available team colors.
     * @return a random {@link TeamColor}
     */
    public static TeamColor chooseColor() {
        Random random = new Random();
        return Team.teamColorPossible.get(random.nextInt(Team.teamColorPossible.size()));
    }

    /**
     * Unlocks a weapon in the team's inventory.
     * @param id the ID of the weapon to unlock
     * @throws IllegalArgumentException if the weapon ID is invalid
     */
    public void unlockWeapon(WeaponId id) throws IllegalArgumentException {
        this.inventory.unlock(id);
    }

    /**
     * Locks a weapon in the team's inventory.
     * @param id the ID of the weapon to lock
     */
    public void lockWeapon(WeaponId id) {
        this.inventory.lock(id);
    }

    /**
     * Adds ammunition for a specific weapon in the team's inventory.
     * @param id weapon for which to add ammunition
     * @param n number of ammo units to add
     * @throws IllegalArgumentException if the weapon ID is invalid
     */
    public void addAmmo(WeaponId id, Integer n) throws IllegalArgumentException {
        this.inventory.addAmmo(id, n);
    }

    /**
     * Fires a projectile using the current worm's selected weapon.
     * @param fireParams parameters for the firing action
     * @return the created {@link Projectile}
     * @throws IllegalArgumentException if the firing parameters are invalid
     */
    public Projectile fire(FireParams fireParams) throws IllegalArgumentException {
        return this.inventory.fire(this.currentWorm.getSelfItem(), fireParams);
    }

    /**
     * Selects a weapon from the inventory for the current worm.
     * @param id weapon ID to select
     */
    public void selectItem(WeaponId id) {
        this.currentWorm.setSelfItem(id);
    }

    /**
     * Switches control to the next worm in the team.
     * If the last worm is reached, it goes to the first worm.
     */
    public void nextWorm() {
        this.index = (++this.index >= this.worms.size()) ? 0 : this.index;
        this.currentWorm = this.getIndexWorm(this.index);
    }
}
