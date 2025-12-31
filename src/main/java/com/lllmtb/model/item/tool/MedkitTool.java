package com.lllmtb.model.item.tool;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class MedkitTool extends Tool {

    private static final int HEAL_AMOUNT = 30;

    @Override
    public ToolId id() {
        return ToolId.MEDKIT;
    }

    @Override
    public List<GameCommand> use(@NonNull UseParams params) {
        Objects.requireNonNull(params);
        GameCommand healCommand = world -> params.owner.damageWorm(-HEAL_AMOUNT);
        return List.of(healCommand);
    }
}