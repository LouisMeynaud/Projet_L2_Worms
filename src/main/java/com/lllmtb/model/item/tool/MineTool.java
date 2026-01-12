package com.lllmtb.model.item.tool;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterEntityCommand;
import com.lllmtb.model.entity.projectile.MineProjectile;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class MineTool extends Tool {

    @Override
    public ToolId id() {
        return ToolId.MINE;
    }

    @Override
    public List<GameCommand> use(@NonNull UseParams params) {
        GameCommand cmd = new RegisterEntityCommand(new MineProjectile(Objects.requireNonNull(params)), params.owner);
        return List.of(cmd);
    }
}
