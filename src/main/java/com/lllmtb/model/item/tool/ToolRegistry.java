package com.lllmtb.model.item.tool;

import com.lllmtb.model.item.ItemRegistry;

public final class ToolRegistry extends ItemRegistry<ToolId, Tool> {

    public ToolRegistry() {
        super(ToolId.class);

        this.register(ToolId.PARACHUTE, new ParachuteTool());
        this.register(ToolId.JETPACK, new JetpackTool());
        this.register(ToolId.CALLSTRIKE, new CallStrikeTool());
        this.register(ToolId.CATCONCRETE, new CatConcreteTool());
    }
}