package com.lllmtb.model.item.tool;


import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterEntityCommand;
import com.lllmtb.model.entity.projectile.GrenadeProjectile;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;


public final class GrenadeTools extends Tool {


    @Override
    public ToolId id() {
        return ToolId.GRENADE;
    }


    @Override
    public List<GameCommand> use(@NonNull UseParams params) {
        GameCommand cmd = new RegisterEntityCommand(new GrenadeProjectile(Objects.requireNonNull(params)), params.owner);
        return List.of(cmd);
    }
}
