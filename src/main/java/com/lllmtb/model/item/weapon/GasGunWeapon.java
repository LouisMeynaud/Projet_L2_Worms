package com.lllmtb.model.item.weapon;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterEntityCommand;
import com.lllmtb.model.entity.projectile.GasProjectile;
import com.lllmtb.model.item.UseParams;

public final class GasGunWeapon extends Weapon {

    @Override
    public WeaponId id() {
        return WeaponId.GASGUN;
    }

    @Override
    public List<GameCommand> fire(@NonNull UseParams params) {
        GameCommand cmd = new RegisterEntityCommand(new GasProjectile(Objects.requireNonNull(params)), params.owner);
        return List.of(cmd);
    }
}
