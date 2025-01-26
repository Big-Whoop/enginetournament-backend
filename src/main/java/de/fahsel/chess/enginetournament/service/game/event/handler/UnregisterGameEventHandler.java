package de.fahsel.chess.enginetournament.service.game.event.handler;

import de.fahsel.chess.enginetournament.model.event.UnregisterGameEvent;
import de.fahsel.chess.enginetournament.service.system.ReactiveProcessRunner;
import org.springframework.stereotype.Service;

@Service
public class UnregisterGameEventHandler implements GameEventHandler<UnregisterGameEvent> {
    private final ReactiveProcessRunner reactiveProcessRunner;

    public UnregisterGameEventHandler(ReactiveProcessRunner reactiveProcessRunner) {
        this.reactiveProcessRunner = reactiveProcessRunner;
    }

    @Override
    public void handle(UnregisterGameEvent gameEvent) {
        reactiveProcessRunner.killAll();
    }
}
