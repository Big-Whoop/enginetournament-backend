package de.fahsel.chess.enginetournament.model.event;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Side;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class InitializeEngineEvent implements InGameEvent {
    Game game;
    String enginePath;
    Side side;
    Map<String, String> uciOptions;
}
