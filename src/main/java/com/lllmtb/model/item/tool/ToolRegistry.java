package com.lllmtb.model.item.tool;

import com.lllmtb.model.item.ItemRegistry;

public final class ToolRegistry extends ItemRegistry<ToolId, Tool> {

    public ToolRegistry() {
        super(ToolId.class);

        this.registry.put(ToolId.PARACHUTE, new ParachuteTool());
    }
}