package com.lllmtb.model.item.tool;

import com.lllmtb.engine.GameWorld;
import com.lllmtb.engine.commands.AddPhysicsModifierCommand;
import com.lllmtb.engine.commands.GameCommand;
import com.lllmtb.events.EventListener;
import com.lllmtb.events.input.ProcessInputsEvent;
import com.lllmtb.events.move.JetpackBoostEvent;
import com.lllmtb.events.physics.PhysicsModifierRemovedEvent;
import com.lllmtb.model.item.UseParams;
import com.lllmtb.physics.PhysicsModifier;
import com.lllmtb.physics.Transform;
import com.lllmtb.physics.Vector;
import org.eclipse.jdt.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class JetpackTool extends Tool {

    private static final double JETPACK_FUEL_DURATION = 5.0; // = 5 seconds of fuel in total
    private static final double JETPACK_THRUST = 20.0;

    @Override
    public ToolId id() {
        return ToolId.JETPACK;
    }

    @Override
    public List<GameCommand> use(@NonNull UseParams params) {
        Objects.requireNonNull(params);

        List<GameCommand> commands = new ArrayList<>();

        Transform transform = (v) -> new Vector(v.getX(), Math.min(v.getY() + JETPACK_THRUST, JETPACK_THRUST));
        PhysicsModifier modifier = new PhysicsModifier(transform, id(), JETPACK_FUEL_DURATION, false);
        commands.add(new AddPhysicsModifierCommand(params.owner, modifier));

        commands.add(new GameCommand() {
            private final AtomicReference<EventListener<JetpackBoostEvent>> boostListenerRef = new AtomicReference<>();
            private final AtomicReference<EventListener<ProcessInputsEvent>> disableListenerRef = new AtomicReference<>();
            private final AtomicReference<EventListener<PhysicsModifierRemovedEvent>> removalListenerRef = new AtomicReference<>();

            @Override
            public void execute(GameWorld world) {
                // Enable the jetpack when boost key is held
                EventListener<JetpackBoostEvent> boostListener = event -> modifier.enabled = true;
                boostListenerRef.set(boostListener);

                // Disable the jetpack at the start of each input cycle
                // (it will be re-enabled by JetpackBoostEvent if the key is still held)
                EventListener<ProcessInputsEvent> disableListener = event -> modifier.enabled = false;
                disableListenerRef.set(disableListener);

                // Clean up listeners when the modifier is removed (out of fuel...)
                EventListener<PhysicsModifierRemovedEvent> removalListener = event -> {
                    if (event.modifier() == modifier && event.body().getEntity() == params.owner) {
                        world.queueCommand(w -> {
                            w.getBus().unsubscribe(JetpackBoostEvent.class, boostListenerRef.get());
                            w.getBus().unsubscribe(ProcessInputsEvent.class, disableListenerRef.get());
                            w.getBus().unsubscribe(PhysicsModifierRemovedEvent.class, removalListenerRef.get());
                        });
                    }
                };
                removalListenerRef.set(removalListener);

                world.getBus().subscribe(JetpackBoostEvent.class, boostListener);
                world.getBus().subscribe(ProcessInputsEvent.class, disableListener);
                world.getBus().subscribe(PhysicsModifierRemovedEvent.class, removalListener);
            }
        });

        return commands;
    }
}

