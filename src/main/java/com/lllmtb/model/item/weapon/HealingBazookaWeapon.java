package com.lllmtb.model.item.weapon;

import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterEntityCommand;
import com.lllmtb.model.entity.projectile.HealingProjectile;
import com.lllmtb.model.item.UseParams;

public final class HealingBazookaWeapon extends Weapon {

    @Override
    public WeaponId id() {
        return WeaponId.HEALINGBAZOOKA;
    }

    @Override
    public List<GameCommand> fire(@NonNull UseParams params) {
        GameCommand cmd = new RegisterEntityCommand(new HealingProjectile(Objects.requireNonNull(params)), params.owner);
        return List.of(cmd);
    }
}
