package com.lllmtb.model.entity.team;

import java.lang.reflect.Field;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.save.TeamSave;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.UnknownItemException;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.model.item.inventory.Inventory;
import com.lllmtb.model.item.tool.ToolId;
import com.lllmtb.model.item.tool.UnknownToolException;
import com.lllmtb.model.item.tool.UnusableToolException;
import com.lllmtb.model.item.weapon.LockedWeaponException;
import com.lllmtb.model.item.weapon.UnknownWeaponException;
import com.lllmtb.model.item.weapon.UnusableWeaponException;
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

    /**
     * List of worms belonging to the team.
     */
    private final ArrayList<Worm> worms = new ArrayList<>();

    /**
     * Inventory containing weapons and items for the team.
     */
    private final transient Inventory inventory;

    /**
     * The name of the team.
     */
    public final String name;

    /**
     * Number of worms in the team.
     */
    private int numberOfWorms;

    /**
     * Unique ID of the team.
     */
    public final int id;

    /**
     * Thread-safe counter for assigning unique team IDs.
     */
    private static final AtomicInteger idCounter = new AtomicInteger(0);

    /**
     * The currently active worm of the team.
     */
    private  Worm currentWorm;

    /**
     * Index of the currently active worm.
     */
    private int index = 0;

    /**
     * The item currently selected by the player
     */
    private ItemId selectedItem;

    /**
     * The color associated with this team.
     */
    public final TeamColor teamColor;

    /**
     * The count of shoot in a turn
     */
    private int shootCount;

    public static final int MAX_CHANGEWORMTEAM = 1;
    private int maxChangeWormTeam = MAX_CHANGEWORMTEAM;

    /**
     * Possible colors that can be assigned to a team.
     */
    private static final List<TeamColor> possibleTeamColors = Arrays.asList(TeamColor.values());
    private static final ArrayList<TeamColor> chosenTeamColors = new ArrayList<>();

    /**
     * The amount of credits (in-game currency) the team has.
     */
    private int credits = 0;

    /**
     * Private constructor used to initialize a team with a given name, number of players, inventory, and spawn position.
     * @param name          the name of the team
     * @param numberOfWorms number of worms in the team
     * @param inventory     the inventory assigned to the team
     * @param spawn         the spawn position where the worms will be placed
     * @param credits       initial amount of credits for the team
     */
    private Team(@NonNull String name, int numberOfWorms, @NonNull Inventory inventory, @NonNull Position[] spawn, int credits) {
        this.teamColor = Team.chooseColor();
        this.inventory = Objects.requireNonNull(inventory);
        this.name = Objects.requireNonNull(name);
        this.numberOfWorms = numberOfWorms;
        this.id = idCounter.incrementAndGet();
        this.setUpWorms(numberOfWorms, spawn);
        this.currentWorm = this.worms.get(0);
        this.selectedItem = null;
        this.credits = credits;
    }

    /**
     * Public constructor used to initialize a team using the private constructor.
     * @param teamName      the name of the team
     * @param numberOfWorms number of worms in the team
     * @param spawn         the spawn position where the worms will be placed
     */
    public Team(@NonNull String teamName, int numberOfWorms, @NonNull Position[] spawn, int credits) {
        this(teamName, numberOfWorms, new Inventory(), spawn, credits);
    }

    /**
     * Public constructor used to initialize a team using the private constructor.
     * @param teamSave      the save of the team
     */
    public Team(@NonNull TeamSave teamSave) {
        this.inventory = new Inventory();
        for (WeaponId weaponId: teamSave.inventorySave.unlockedWeapons) {
            inventory.unlock(weaponId);
        }
        for (WeaponId weaponId: teamSave.inventorySave.weaponAmmo.keySet()) {
            inventory.weapons.addAmmo(weaponId, teamSave.inventorySave.weaponAmmo.get(weaponId));
        }
        for (ToolId toolId: teamSave.inventorySave.toolAmmo.keySet()) {
            inventory.tools.addAmmo(toolId, teamSave.inventorySave.toolAmmo.get(toolId));
        }

        this.name = teamSave.team.name;
        this.numberOfWorms =  teamSave.team.numberOfWorms;
        this.id = teamSave.team.id;
        this.credits = teamSave.team.credits;
        for (Worm worm: teamSave.team.getTeamWorms()) {
            this.worms.add(worm);
        }
        this.selectedItem = null;
        this.teamColor = teamSave.team.teamColor;
        // use reflexion to recreate worm instance with correct PhysicsBody
        for (Worm worm : this.worms) {
                try {
                    Field teamField = Worm.class.getDeclaredField("team");
                    teamField.setAccessible(true);
                    teamField.set(worm, this);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        this.index = teamSave.team.index;
        this.currentWorm = this.worms.get(this.index);
    }

    /**
         * Returns the name associated with the team.
         * @return the team's name
         */
    public String getName() {
        return this.name;
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
    public int getNumberOfWorms() {
        return this.numberOfWorms;
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
     * @return the count of shoot in this turn
     */
    public int getShootCount() {
        return this.shootCount;
    }

    /**
     * @return the changeWorm left of the team
     */
    public int getMaxChangeWormTeam() {
        return maxChangeWormTeam;
    }

    /**
     * Remove one usage of change worm of the team
     */
    public void oneChangeWorm() {
        this.maxChangeWormTeam--;
    }

    public static ArrayList<TeamColor> getChosenTeamColors() {
        return chosenTeamColors;
    }

    /**
     * Reset shoot count
     */
    public void resetShootCount() {
        this.shootCount = 0;
    }

    /**
     * Reset worm change count
     */
    public void resetWormChangeCount() {
        this.maxChangeWormTeam = MAX_CHANGEWORMTEAM;
    }

    /**
     * Creates and adds worms to the team based on the number of players.
     * @param numberOfPlayers number of worms to create
     * @param spawns          all spawn position for all worms
     */
    private void setUpWorms(int numberOfPlayers, @NonNull Position[] spawns) {
        Objects.requireNonNull(spawns);
        if (spawns.length != numberOfWorms) {
            throw new IllegalArgumentException("Team spawnpoint array does not match the number of worms!");
        }
        for (int i = 1; i <= numberOfPlayers; i++) {
            new Worm(this.name + i, this);
            Position spawn = spawns[i - 1];
            this.worms.get(i - 1).spawn(spawn);
        }
    }

    /**
     * Adds a new worm to the team.
     * @param worm the worm to add
     */
    public void addWorm(@NonNull Worm worm) {
        this.worms.add(Objects.requireNonNull(worm));
        this.numberOfWorms = this.worms.size();
    }

    /**
     * Removes a worm from the team based on its index.
     * @param indexPlayer index of the worm to remove
     */
    public void removeWorm(int indexPlayer) {
        this.worms.remove(indexPlayer - 1);
        this.numberOfWorms = this.worms.size();
    }

    public void removeWorm(Worm worm) {
        this.worms.remove(worm);
        this.numberOfWorms = this.worms.size();
    }

    public boolean oneAlive() {
        for (Worm worm : this.worms) {
            if (worm.isAlive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Randomly selects a color from the available team colors.
     * @return a random {@link TeamColor}
     */
    public static TeamColor chooseColor() {
        Random random = new Random();
        if (chosenTeamColors.size() == possibleTeamColors.size()) {
            Team.chosenTeamColors.clear();
        }
        TeamColor teamcolor = Team.possibleTeamColors.get(random.nextInt(Team.possibleTeamColors.size()));
        while (chosenTeamColors.contains(teamcolor)) {
            teamcolor = Team.possibleTeamColors.get(random.nextInt(Team.possibleTeamColors.size()));
        }
        chosenTeamColors.add(teamcolor);
        return teamcolor;
    }

    /**
     * Unlocks a weapon in the team's inventory.
     * @param id the ID of the weapon to unlock
     * @throws UnknownWeaponException if the weapon type is unknown
     */
    public void unlockWeapon(@NonNull WeaponId id) {
        Logger.logger.info("[TEAM {}] Unlocking weapon {}", this.name, id);
        this.inventory.unlock(id);
    }

    /**
     * Locks a weapon in the team's inventory.
     * @param id the ID of the weapon to lock
     * @throws UnknownWeaponException if the weapon type is unknown
     */
    public void lockWeapon(@NonNull WeaponId id) {
        Logger.logger.info("[TEAM {}] Locking weapon {}", this.name, id);
        this.inventory.lock(id);
    }

    /**
     * Adds ammunition for a specific weapon in the team's inventory.
     * @param id weapon for which to add ammunition
     * @param n  number of ammo units to add
     * @throws UnknownWeaponException if the weapon type is unknown
     */
    public void addAmmo(@NonNull WeaponId id, int n) {
        try {
            Logger.logger.info("[TEAM {}] Adding {} ammo to weapon {}", this.name, n, id);
            this.inventory.weapons.addAmmo(id, n);
        } catch (UnknownItemException e) {
            throw new UnknownWeaponException((WeaponId) e.id);
        }
    }

    /**
     * Adds ammunition for a specific tool in the team's inventory.
     * @param id tool for which to add ammunition
     * @param n  number of ammo units to add
     * @throws UnknownToolException if the tool type is unknown
     */
    public void addAmmo(@NonNull ToolId id, int n) {
        try {
            Logger.logger.info("[TEAM {}] Adding {} ammo to tool {}", this.name, n, id);
            this.inventory.tools.addAmmo(id, n);
        } catch (UnknownItemException e) {
            throw new UnknownToolException((ToolId) e.id);
        }
    }

    /**
     * Uses the currently selected item (weapon or tool).
     * Dispatches to the appropriate Inventory.use method based on the runtime type of the selected item.
     *
     * @param useParams parameters for the use action
     * @return {@code null} if no item is selected, else the created commands to execute
     * @throws NullPointerException if useParams is null
     * @throws UnknownItemException if the selected item is not a ToolId or WeaponId
     */
    public List<GameCommand> use(@NonNull UseParams useParams) {
        Objects.requireNonNull(useParams);
        if (this.selectedItem == null) return List.of();
        if (this.selectedItem instanceof WeaponId weaponId) {
            Logger.logger.info("[TEAM {}] Firing weapon {}", this.name, weaponId);
            this.shootCount++;
            return this.inventory.fire(weaponId, useParams);
        }
        if (this.selectedItem instanceof ToolId toolId) {
            Logger.logger.info("[TEAM {}] Using tool {}", this.name, toolId);
            this.emptyHand();
            return this.inventory.use(toolId, useParams);
        }
        throw new UnknownItemException(this.selectedItem);
    }

    /**
     * @return the currently selected item
     */
    public ItemId getHand() {
        return this.selectedItem;
    }

    /**
     * Sets the currently selected item to null
     */
    public void emptyHand() {
        this.selectedItem = null;
    }

    /**
     * Selects an item from the inventory.
     * @param id item to select
     */
    private void selectItem(@NonNull ItemId id) {
        this.selectedItem = Objects.requireNonNull(id);
    }

    /**
     * Selects a weapon from the inventory.
     * @param id weapon to select
     */
    public void selectWeapon(WeaponId id) {
        if (!inventory.weapons.contains(id)) {
            throw new UnknownWeaponException(id);
        }
        if (!inventory.isUnlocked(id)) {
            throw new LockedWeaponException(id, this);
        }
        if (!inventory.canFire(id)) {
            throw new UnusableWeaponException(id, this);
        }
        Logger.logger.info("[TEAM {}] Selecting weapon {}", this.name, id);
        selectItem(id);
    }

    /**
     * Selects a tool from the inventory.
     * @param id tool to select
     */
    public void selectTool(ToolId id) {
        if (!inventory.tools.contains(id)) {
            throw new UnknownToolException(id);
        }
        if (!inventory.canUse(id)) {
            throw new UnusableToolException(id, this);
        }
        Logger.logger.info("[TEAM {}] Selecting tool {}", this.name, id);
        selectItem(id);
    }

    /**
     * Switches control to the next worm in the team.
     * If the last worm is reached, it goes to the first worm.
     */
    public void nextWorm() {
        this.index = (++this.index >= this.worms.size()) ? 0 : this.index;
        this.currentWorm = this.getIndexWorm(this.index);
    }

    /**
     * Set currentWorm of the team to null
     */
    public void setCurrentWormNull() {
        this.currentWorm = null;
    }

    /**
     * @return the {@link TeamColor} of the team
     */
    public TeamColor getTeamColor() {
        return this.teamColor;
    }


    public int getCredits() {
        return credits;
    }

    public void addCredits(int amount) {
        if (amount > 0 && credits != -1) {
            Logger.logger.info("[TEAM {}] Adding {} credits", this.name, amount);
            this.credits += amount;
        }
    }

    public void removeCredits(int amount) {
        if (credits == -1) return;

        if (amount > 0 && credits >= amount) {
            Logger.logger.info("[TEAM {}] Removing {} credits", this.name, amount);
            this.credits -= amount;
        }
    }
}