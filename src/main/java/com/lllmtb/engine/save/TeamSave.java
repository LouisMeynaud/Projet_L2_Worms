package com.lllmtb.engine.save;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.team.TeamColor;
import com.lllmtb.model.entity.worm.Worm;
import com.lllmtb.model.item.ItemId;
import java.util.ArrayList;

public class TeamSave {

    protected  Team team;
    protected  InventorySave inventorySave;

    public  TeamSave() {

    }
    public TeamSave(Team team) {
        this.team = team;
        this.inventorySave = new InventorySave(team.getInventory());
    }
}
