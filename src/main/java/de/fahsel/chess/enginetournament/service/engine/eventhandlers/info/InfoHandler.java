package de.fahsel.chess.enginetournament.service.engine.eventhandlers.info;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.service.engine.eventhandlers.util.EngineResponseTokens;

public interface InfoHandler {
    void handle(EngineResponseTokens info, Engine engine);
    boolean handles(EngineResponseTokens info);
}
