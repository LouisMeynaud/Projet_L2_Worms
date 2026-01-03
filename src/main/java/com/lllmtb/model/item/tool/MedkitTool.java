package com.lllmtb.model.item.tool;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MedkitTool extends Tool {

    private static final int HEAL_AMOUNT = 50;

    @Override
    public ToolId id() {
        return ToolId.MEDKIT;
    }

    @Override
    public List<GameCommand> use(@NonNull UseParams params) {
        List<GameCommand> list = new ArrayList<>();
        Objects.requireNonNull(params);
        list.add(world -> params.owner.damageWorm(-HEAL_AMOUNT));
        list.add(params.owner.removeAllStatusEffects());
        return list;
    }
}