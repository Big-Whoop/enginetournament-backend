package de.fahsel.chess.enginetournament.service.engine.eventhandlers;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.service.engine.eventhandlers.info.InfoHandler;
import de.fahsel.chess.enginetournament.service.engine.eventhandlers.util.EngineResponseTokenizer;
import de.fahsel.chess.enginetournament.service.engine.eventhandlers.util.EngineResponseTokens;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Info implements EngineEventHandler {
    private final List<InfoHandler> infoHandlers;
    private final EngineResponseTokenizer engineResponseTokenizer;

    public Info(List<InfoHandler> infoHandlers, EngineResponseTokenizer engineResponseTokenizer) {
        this.infoHandlers = infoHandlers;
        this.engineResponseTokenizer = engineResponseTokenizer;
    }

    @Override
    public void handle(String lineFromEngine, Engine engine) {
        EngineResponseTokens engineResponseTokens = engineResponseTokenizer.get(lineFromEngine);

        infoHandlers.stream()
                .filter(infoHandler -> infoHandler.handles(engineResponseTokens))
                .forEach(infoHandler -> infoHandler.handle(engineResponseTokens, engine));
    }

    @Override
    public String command() {
        return "info";
    }
}
