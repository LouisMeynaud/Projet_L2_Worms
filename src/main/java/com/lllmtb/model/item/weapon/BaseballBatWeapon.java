package com.lllmtb.model.item.weapon;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.PushEntitiesCommand;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.Position;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public final class BaseballBatWeapon extends Weapon {
    private static final double PUSH_RADIUS = 5.0;
    private static final double PUSH_FORCE = 500.0;

    @Override
    public WeaponId id() {
        return WeaponId.BASEBALLBAT;
    }

    @Override
    public List<GameCommand> fire(@NonNull UseParams params) {
        Objects.requireNonNull(params);

        Position wormPos = params.owner.getBody().getPos();
        double centerX = wormPos.getX() + (params.owner.getBody().getWidth() / 2.0);
        double centerY = wormPos.getY() + (params.owner.getBody().getHeight() / 2.0);

        GameCommand cmd = new PushEntitiesCommand(
            new Position(centerX, centerY),
            params.angleRadians,
            PUSH_RADIUS,
            PUSH_FORCE,
            params.owner
        );

        return List.of(cmd);
    }
}

