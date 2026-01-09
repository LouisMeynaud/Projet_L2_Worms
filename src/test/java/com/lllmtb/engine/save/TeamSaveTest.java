package com.lllmtb.engine.save;

import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeamSaveTest {
    @Test
    public void ConstructorTest() {
        Position position = new Position(3,2);
        Team team = new Team("test",1, new Position[]{position},150);
        TeamSave teamSave = new TeamSave(team);
        Assertions.assertNotNull(teamSave);
        assertEquals(team.id, teamSave.team.id);
        assertEquals(team.name, teamSave.team.name);
        assertEquals(team, teamSave.team);
    }
}
