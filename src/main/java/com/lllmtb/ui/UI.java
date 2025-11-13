package com.lllmtb.ui;

import com.lllmtb.engine.GameSettings;

abstract class UI {
    protected Object world;
    public UI(){
        world = null;
    }
    abstract GameSettings promptSettings();
    abstract boolean readyToPlay();

}
