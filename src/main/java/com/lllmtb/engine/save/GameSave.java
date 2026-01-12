package com.lllmtb.engine.save;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.turn.TurnManager;
import com.lllmtb.model.entity.projectile.Projectile;
import com.lllmtb.model.entity.team.Team;
import com.lllmtb.model.entity.worm.Statistics;
import com.lllmtb.physics.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameSave {
    public final CopyOnWriteArrayList<PhysicsBodySave> entities;
    public final int indexTeam;
    public final long turnDuration;
    public final List<TeamSave> teams;
    public final boolean hasWind;
    public final Vector wind;
    public final Vector gravity;
    public final GameSettings gameSettings;
    public final TurnManager turnManager;


    public GameSave(GameWorld gameWorld) {
        this.teams = new ArrayList<>();
        for (Team team : gameWorld.getTeams()) {
            this.teams.add(new TeamSave(team));
        }
        this.turnManager = gameWorld.getTurnManager();
        this.indexTeam = gameWorld.getIndexTeam();
        this.turnDuration = gameWorld.getTurnDuration() / 1000;
        this.hasWind = gameWorld.getPhysics().hasWind();
        this.wind = gameWorld.getPhysics().getWind();
        this.gravity = gameWorld.getPhysics().getGravity();
        this.entities = new CopyOnWriteArrayList<>();
        for (PhysicsBody body : gameWorld.getPhysics().getBodies()) {
            entities.add(new PhysicsBodySave(body));
        }
        String[] teamNames = new String[gameWorld.getTeams().size()];
        for (int i = 0; i < teamNames.length; i++) {
            teamNames[i] = gameWorld.getTeams().get(i).name;
        }
        boolean[] isBot = new boolean[gameWorld.getTeams().size()];
        for (int i = 0; i < isBot.length; i++) {
            isBot[i] = gameWorld.getTeams().get(i).isBot();
        }

        this.gameSettings = new GameSettings(
                gameWorld.getGameName(),
                gameWorld.getTeams().size(),
                teamNames,
                isBot,
                1,
                Statistics.getMaxHp(),
                gameWorld.getTurnDuration(),
                gameWorld.getPhysics().hasWind(),
                gameWorld.getGameName(),
                gameWorld.getConfigMap(),
                Move.getMaxjump(),
                gameWorld.getGameMode(),
                Projectile.isFriendlyfire(),
            false);
    }

    @Override
    public String toString() {
        for (PhysicsBodySave bodySave : this.entities) {

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