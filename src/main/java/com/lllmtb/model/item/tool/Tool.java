package com.lllmtb.model.item.tool;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.item.Item;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;

public abstract class Tool implements Item {
    public abstract ToolId id();

    public abstract List<GameCommand> use(@NonNull UseParams params);
}
