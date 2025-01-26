package de.fahsel.chess.enginetournament.model.event;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.game.Game;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class EngineReadyEvent implements InGameEvent {
    Game game;
    Engine engine;
}
