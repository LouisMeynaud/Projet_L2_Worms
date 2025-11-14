package com.lllmtb.ui;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.GameWorld;

import java.io.IOException;

abstract class UI {
    protected GameWorld world;
    public UI(){
        world = null;
    }
    abstract GameSettings promptSettings() throws IOException;
    abstract boolean readyToPlay();

    protected void setWorld(GameWorld world) {
        this.world = world;
    }

    abstract void close();
}
