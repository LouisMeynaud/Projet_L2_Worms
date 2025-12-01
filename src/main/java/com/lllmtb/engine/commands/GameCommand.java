package com.lllmtb.engine.commands;

import com.lllmtb.engine.GameWorld;

/**
 * Represents a command that can be executed within the game world.
 */
public interface GameCommand {

    /**
     * Executes the command within the provided game world context.
     * @param world the game world in which to execute the command
     */
    void execute(GameWorld world);
}