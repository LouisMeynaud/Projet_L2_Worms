package com.lllmtb.ui;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;

import java.io.IOException;

public abstract class UI {
    protected GameWorld world;

    public UI(){
        world = null;
    }

    public abstract GameSettings promptSettings() throws IOException;

    public void setWorld(GameWorld world) {
        this.world = world;
    }

    public abstract boolean readyToPlay();

    public abstract void close();
}
