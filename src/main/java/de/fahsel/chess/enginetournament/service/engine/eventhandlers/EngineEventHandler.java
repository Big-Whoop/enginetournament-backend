package de.fahsel.chess.enginetournament.service.engine.eventhandlers;

import de.fahsel.chess.enginetournament.model.engine.Engine;

public interface EngineEventHandler {
    void handle(String lineFromEngine, Engine engine);
    String command();
}
