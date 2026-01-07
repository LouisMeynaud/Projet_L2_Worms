package com.lllmtb.model.item.weapon;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterEntityCommand;
import com.lllmtb.model.entity.projectile.RocketProjectile;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public final class BazookaWeapon extends Weapon {

    @Override
    public WeaponId id() {
        return WeaponId.BAZOOKA;
    }

    @Override
    public List<GameCommand> fire(@NonNull UseParams params) {
        GameCommand cmd = new RegisterEntityCommand(new RocketProjectile(Objects.requireNonNull(params)), params.owner);
        return List.of(cmd);
    }
}
