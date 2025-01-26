package de.fahsel.chess.enginetournament.model.tournament;

import de.fahsel.chess.enginetournament.model.game.GameState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Match {
    private String engineIdWhite;
    private String engineIdBlack;

    private GameState result;
    private GameState.Reason reason;
}
