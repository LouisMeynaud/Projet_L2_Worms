package com.lllmtb.engine.Save;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.physics.PhysicsBody;
import com.lllmtb.physics.PhysicsModifier;
import com.lllmtb.physics.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GameSave {
    private EventBus bus;
    private int indexTeam;
    private long turnDuration;
    private List<Team> teams;
    private boolean hasWind;
    private Vector wind;
    private Vector gravity;
    private ConcurrentHashMap<PhysicsBody, ArrayList<PhysicsModifier>> modifiers;


    public GameSave(GameWorld gameWorld) {
        this.teams = gameWorld.getTeams();
        this.indexTeam = gameWorld.getIndexTeam();
        this.turnDuration = gameWorld.getTurnDuration();
        this.bus = gameWorld.getBus();
        this.modifiers = gameWorld.getPhysics().getModifiers();
        this.hasWind = gameWorld.getPhysics().hasWind();
        this.wind = gameWorld.getPhysics().getWind();
        this.gravity = gameWorld.getPhysics().getGravity();
    }


    public GameSave() {

    }

}