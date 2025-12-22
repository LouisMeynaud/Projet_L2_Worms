package com.lllmtb.model.item.weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.engine.commands.RegisterProjectileCommand;
import com.lllmtb.model.entity.projectile.ShotGunProjectile;
import com.lllmtb.model.item.UseParams;

public final class ShotGunWeapon extends Weapon {

    @Override
    public WeaponId id() {
        return WeaponId.SHOTGUN;
    }

    @Override
    public List<GameCommand> fire(@NonNull UseParams params) {
        Objects.requireNonNull(params);
        List<GameCommand> commands = new ArrayList<>();
        for (double i = -0.2; i <= 0.2; i += 0.1) {
            UseParams param = new UseParams(params.angleRadians + i, params.pos, params.owner);
            GameCommand commande = new RegisterProjectileCommand(new ShotGunProjectile(param), params.owner);
            commands.add(commande);
        }
        return commands;
    }
}
