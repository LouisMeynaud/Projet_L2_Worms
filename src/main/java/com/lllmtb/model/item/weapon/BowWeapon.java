package com.lllmtb.model.item.weapon;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterProjectileCommand;
import com.lllmtb.model.entity.projectile.BowProjectile;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public final class BowWeapon extends Weapon {

    @Override
    public WeaponId id() {
        return WeaponId.BOW;
    }

    @Override
    public List<GameCommand> fire(@NonNull UseParams params) {
        GameCommand cmd = new RegisterProjectileCommand(new BowProjectile(Objects.requireNonNull(params)), params.owner);
        return List.of(cmd);
    }
}
