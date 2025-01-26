package de.fahsel.chess.enginetournament.model.event;

import de.fahsel.chess.enginetournament.model.game.Game;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MakeMoveEvent implements InGameEvent {
    Game game;
}
