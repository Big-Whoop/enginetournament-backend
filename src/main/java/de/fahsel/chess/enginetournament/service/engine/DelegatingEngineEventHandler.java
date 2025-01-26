package de.fahsel.chess.enginetournament.service.engine;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.event.EngineEventReceivedEvent;
import de.fahsel.chess.enginetournament.service.engine.eventhandlers.EngineEventHandler;
import de.fahsel.chess.enginetournament.service.game.event.GameEventStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class DelegatingEngineEventHandler {
    private final Map<String, EngineEventHandler> engineEventHandlers = new HashMap<>();
    private final GameEventStream gameEventStream;

    public DelegatingEngineEventHandler(List<EngineEventHandler> engineEventHandlers, GameEventStream gameEventStream) {
        this.gameEventStream = gameEventStream;
        engineEventHandlers.forEach(
                engineEventHandler -> this.engineEventHandlers.put(engineEventHandler.command(), engineEventHandler)
        );
    }

    public void handle(String lineFromEngine, Engine engine) {
        String command = getCommand(lineFromEngine);

        if (engineEventHandlers.containsKey(command)) {
            engineEventHandlers.get(command).handle(lineFromEngine, engine);
        }

        EngineEventReceivedEvent engineEventReceivedEvent = EngineEventReceivedEvent.builder()
                .game(engine.getGame())
                .build();

        gameEventStream.publish(engineEventReceivedEvent);
    }

    private String getCommand(String lineFromEngine) {
        return lineFromEngine.toLowerCase().split(" ")[0];
    }
}
