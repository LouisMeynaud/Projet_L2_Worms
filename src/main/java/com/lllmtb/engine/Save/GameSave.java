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
import java.util.concurrent.CopyOnWriteArrayList;

public class GameSave {
    private CopyOnWriteArrayList<PhysicsBody> entities;
    private transient EventBus bus;
    private int indexTeam;
    private long turnDuration;
    private List<TeamSave> teams = new ArrayList<>();
    private boolean hasWind;
    private Vector wind;
    private Vector gravity;
    private ConcurrentHashMap<Integer, ArrayList<PhysicsModifier>> modifiers;


    public GameSave(GameWorld gameWorld) {
        for (Team team : gameWorld.getTeams()) {
            this.teams.add(new TeamSave(team));
        }
        this.indexTeam = gameWorld.getIndexTeam();
        this.turnDuration = gameWorld.getTurnDuration();
        this.bus = gameWorld.getBus();
        this.hasWind = gameWorld.getPhysics().hasWind();
        this.wind = gameWorld.getPhysics().getWind();
        this.gravity = gameWorld.getPhysics().getGravity();
        this.modifiers = new ConcurrentHashMap<>();
        gameWorld.getPhysics().getModifiers().forEach((body, mods) -> {
           this.modifiers.put(body.getEntity().uuid, mods);
        });
        this.entities = gameWorld.getPhysics().getBodies();
    }


    public GameSave() {

    }

}