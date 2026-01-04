package com.lllmtb.engine.save;

import com.lllmtb.model.entity.team.Team;

public class TeamSave {

    public  Team team;
    public  InventorySave inventorySave;

    public  TeamSave() {

    }
    public TeamSave(Team team) {
        this.team = team;
        this.inventorySave = new InventorySave(team.getInventory());
    }
}
