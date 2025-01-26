package de.fahsel.chess.enginetournament.model.event;

import de.fahsel.chess.enginetournament.model.game.Game;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class GameReadyEvent implements InGameEvent {
    Game game;
}
