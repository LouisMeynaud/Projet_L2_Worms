package com.lllmtb.engine;

public class GameEngine {

    public static void main(String[] args) {
        // With default values
        String[] teamName = {"Blue", "Red"};
        GameSettings settings = new GameSettings(2, teamName ,1,false, "TestMap", false );
        GameWorld world = new GameWorld(settings);
        GameLoop loop = new GameLoop();



        //With Interface



    }
}
