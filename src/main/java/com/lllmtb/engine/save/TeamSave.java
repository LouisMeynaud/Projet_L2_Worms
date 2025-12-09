package com.lllmtb.engine.save;

import com.lllmtb.model.entity.team.Team;

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
