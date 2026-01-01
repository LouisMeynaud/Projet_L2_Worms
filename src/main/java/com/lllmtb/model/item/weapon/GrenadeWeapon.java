package com.lllmtb.model.item.weapon;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterProjectileCommand;
import com.lllmtb.model.entity.projectile.GrenadeProjectile;
import com.lllmtb.model.item.UseParams;
import org.eclipse.jdt.annotation.NonNull;

import java.util.List;
import java.util.Objects;


public final class GrenadeWeapon extends Weapon {


    @Override
    public WeaponId id() {
        return WeaponId.GRENADE;
    }


    @Override
    public List<GameCommand> fire(@NonNull UseParams params) {
        GameCommand cmd = new RegisterProjectileCommand(new GrenadeProjectile(Objects.requireNonNull(params)), params.owner);
        return List.of(cmd);
    }
}
