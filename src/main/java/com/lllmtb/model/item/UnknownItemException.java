package com.lllmtb.model.item;

public class UnknownItemException extends RuntimeException {
    public final ItemId id;

    public UnknownItemException(ItemId id) {
        super("No item was found for id: " + id);
        this.id = id;
    }
}
