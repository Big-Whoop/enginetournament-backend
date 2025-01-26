package de.fahsel.chess.enginetournament.model.event;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Move;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MadeMoveEvent implements InGameEvent {
    Game game;
    Move move;
}
