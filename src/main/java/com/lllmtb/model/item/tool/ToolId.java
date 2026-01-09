package com.lllmtb.model.item.tool;

import com.lllmtb.model.item.ItemId;
import com.lllmtb.model.item.ShootMode;

public enum ToolId implements ItemId {
    PARACHUTE(ShootMode.NONE, "Déploie un parachute pour une descente sûre, même en cas de vent."),
    JETPACK(ShootMode.NONE, "Volez librement sur la carte. Surveillez la jauge de carburant !"),
    CALLSTRIKE(ShootMode.CROSSHAIR, "Lancer une frappe aérienne dévastatrice à l'aide de missiles sur une zone ciblée.", 0),
    CATCONCRETE(ShootMode.CROSSHAIR, "Invoque un chat de pierre maléfique qui détruit tout ce qui se trouve sous ses pattes.", 0),
    TOWER("Déploie une tourelle défensive pour intercepter les ennemis qui s'approchent de vous lors des prochains tours.", 3000),
    MINE("Un piège explosif qui détonne instantanément si quelqu'un ose marcher dessus.", 3000),
    GRENADE("Une bombe à retardement qui rebondit sur le terrain avant d'exploser.", 4000),
    MEDKIT(ShootMode.NONE, "Restaure une partie de la santé du ver actif."),
    TRAP("Un piège jetable qui inflige du poison.", 3000),
    NONE;

    private final ShootMode shootMode;
    private final String info;
    private final int maxCharge;

    ToolId(ShootMode shootMode, String info, int maxCharge) {
        this.shootMode = shootMode;
        this.info = info;
        this.maxCharge = maxCharge;
    }

    ToolId(ShootMode shootMode, String info) {
        this(shootMode, info, 0);
    }

    ToolId(String info, int maxCharge) {
        this(ShootMode.AIM, info, maxCharge);
    }

    ToolId(String info) {
        this(ShootMode.AIM, info, 0);
    }

    ToolId() {
        this(ShootMode.AIM, "", 0);
    }

    @Override
    public ShootMode getShootMode() {
        return shootMode;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public int getMaxChargeTime() {
        return this.maxCharge;
    }
}
