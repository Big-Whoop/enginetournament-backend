package de.fahsel.chess.enginetournament.service.engine.eventhandlers;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.event.EngineReadyEvent;
import de.fahsel.chess.enginetournament.service.game.event.GameEventStream;
import org.springframework.stereotype.Service;

@Service
public class ReadyOk implements EngineEventHandler {
    private final GameEventStream gameEventStream;

    public ReadyOk(GameEventStream gameEventStream) {
        this.gameEventStream = gameEventStream;
    }

    @Override
    public void handle(String lineFromEngine, Engine engine) {
        EngineReadyEvent engineReadyEvent = EngineReadyEvent.builder()
            .engine(engine)
            .game(engine.getGame())
            .build();

        gameEventStream.publish(engineReadyEvent);
    }

    @Override
    public String command() {
        return "readyok";
    }
}
