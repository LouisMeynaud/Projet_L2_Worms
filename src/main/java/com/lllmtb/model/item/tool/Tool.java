package com.lllmtb.model.item.tool;
import com.lllmtb.model.entity.Entity;
import com.lllmtb.model.item.IItems;

public abstract class Tool implements IItems {

    private final int test;

    public Tool(int test) {
        this.test = test;
    }

    public int getTest() {
        return test;
    }
}