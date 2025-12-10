package com.lllmtb.engine.save;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.events.EventBus;
import com.lllmtb.ui.UI;
import com.lllmtb.ui.graphical.GraphicalUI;

import java.io.FileNotFoundException;

public class LoadTest{
    public static void main(String[] args) throws FileNotFoundException {
        UI ui = new GraphicalUI(new EventBus());
        ui.start();
        GameWorld world  =  Load.loadAll("null");
        world.start();

    }
}
