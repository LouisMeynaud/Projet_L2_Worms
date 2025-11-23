package com.lllmtb.engine.commands;

import com.lllmtb.engine.GameWorld;

public interface GameCommand {

    void execute(GameWorld world);
}