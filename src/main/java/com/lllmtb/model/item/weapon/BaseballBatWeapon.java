package com.lllmtb.model.item.weapon;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.PushEntitiesCommand;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public final class BaseballBatWeapon extends Weapon {
    private static final double PUSH_RADIUS = 5.0;
    private static final double PUSH_FORCE = 200.0;

    @Override
    public WeaponId id() {
        return WeaponId.BASEBALLBAT;
    }

    @Override
    public List<GameCommand> fire(@NonNull UseParams params) {
        Objects.requireNonNull(params);

        GameCommand cmd = new PushEntitiesCommand(
            params.pos,
            params.angleRadians,
            PUSH_RADIUS,
            PUSH_FORCE,
            params.owner
        );

        return List.of(cmd);
    }
}

