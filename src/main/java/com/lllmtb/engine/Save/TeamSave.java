package com.lllmtb.engine.Save;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.team.TeamColor;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamSave {
    private final ArrayList<Worm> worms = new ArrayList<>();

    /**
     * Inventory containing weapons and items for the team.
     */
    private  InventorySave inventory;

    /**
     * The name of the team.
     */
    private String name;

    /**
     * Number of worms in the team.
     */
    private int numberOfWorms;

    /**
     * Unique ID of the team.
     */
    private int id;

    /**
     * Static counter for assigning unique team IDs.
     */
    private static int ids;

    /**
     * The currently active worm of the team.
     */
    private Worm currentWorm;

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
    public  TeamColor teamColor;

    /**
     * Possible colors that can be assigned to a team.
     */
    private static final List<TeamColor> possibleTeamColors = Arrays.asList(TeamColor.values());
    private static final ArrayList<TeamColor> chosenTeamColors = new ArrayList<>();


    public  TeamSave() {

    }
    public TeamSave(Team team) {
        this.teamColor = team.teamColor;
        this.name = team.name;
        this.id = this.ids++;
        this.currentWorm = team.getCurrentWorm();
        this.inventory = new InventorySave(team.getInventory());
    }
}
