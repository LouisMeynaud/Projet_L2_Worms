package com.lllmtb.model.item.tool;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.item.Item;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;

/**
 * Immutable descriptor + factory for creating tool usage actions.
 */
public abstract class Tool implements Item {
    public abstract ToolId id();
    // + metadata (sprites, audio...)

    /**
     * Create commands for this tool usage action.
     * If usage is not allowed for other reasons this should still return commands
     * or higher-level Inventory.use() should have prevented the call.
     */
    public abstract List<GameCommand> use(@NonNull UseParams params);
}
