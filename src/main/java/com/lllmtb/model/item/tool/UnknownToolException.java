package com.lllmtb.model.item.tool;

import com.lllmtb.model.item.UnknownItemException;

public class UnknownToolException extends UnknownItemException {

    public UnknownToolException(ToolId id) {
        super(id);
    }
}
