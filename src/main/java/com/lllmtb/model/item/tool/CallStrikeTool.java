package com.lllmtb.model.item.tool;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterEntityCommand;
import com.lllmtb.model.entity.projectile.MissileProjectile;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class CallStrikeTool extends Tool {

    @Override
    public ToolId id() {
        return ToolId.CALLSTRIKE;
    }

    @Override
    public List<GameCommand> use(@NonNull UseParams params) {
        GameCommand cmd = new RegisterEntityCommand(new MissileProjectile(Objects.requireNonNull(params)), params.owner);
        return List.of(cmd);
    }
}
