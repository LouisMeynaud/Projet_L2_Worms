package com.lllmtb.model.entity;

public abstract class Entity {

    // Champs
    private static int uuids = 0;
    public final int uuid;

    public Entity(){
        this.uuid = ++Entity.uuids;
    }




}
