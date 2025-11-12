package com.lllmtb.ui;

import com.lllmtb.engine.GameSettings;

abstract class UI {
    Object world;
    abstract void init();
    abstract GameSettings promptSettings();
    abstract boolean readyToPlay();

}
