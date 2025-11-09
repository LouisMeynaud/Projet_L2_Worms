package com.lllmtb.model.item.tool;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.IItems;

public abstract class Tool implements IItems {

    private final int id;

    public Tool(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}