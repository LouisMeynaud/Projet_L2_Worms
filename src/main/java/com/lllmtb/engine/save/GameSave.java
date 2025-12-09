package com.lllmtb.engine.save;

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
    protected CopyOnWriteArrayList<PhysicsBodySave> entities = new CopyOnWriteArrayList<>();
    protected transient EventBus bus;
    protected int indexTeam;
    protected long turnDuration;
    protected List<TeamSave> teams = new ArrayList<>();
    protected boolean hasWind;
    protected Vector wind;
    protected Vector gravity;
    protected ConcurrentHashMap<Integer, ArrayList<PhysicsModifier>> modifiers;


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
        for (PhysicsBody body : gameWorld.getPhysics().getBodies()) {
            entities.add(new PhysicsBodySave(body));
        }
    }


    public GameSave() {

    }

    @Override
    public String toString() {
        System.out.println(this.modifiers.toString());
        for(PhysicsBodySave bodySave : this.entities){
            System.out.println(bodySave.toString());
            System.out.println(bodySave.entity.toString());
            System.out.println(bodySave.physicsBody.toString());
            System.out.println(bodySave.entity.getBody().toString());
        }
        System.out.println(this.turnDuration);
        System.out.println(this.gravity.toString());
        System.out.println(this.indexTeam);
        System.out.println(this.wind.toString());
        return "";
    }
}