package com.lllmtb.model.item.tool;
import com.lllmtb.model.item.Item;

public abstract class Tool implements Item {

    private final int id;

    public Tool(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}