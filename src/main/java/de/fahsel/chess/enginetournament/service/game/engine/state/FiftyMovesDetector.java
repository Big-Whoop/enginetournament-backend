package de.fahsel.chess.enginetournament.service.game.engine.state;

import de.fahsel.chess.enginetournament.model.game.Game;
import org.springframework.stereotype.Service;

import static de.fahsel.chess.enginetournament.model.game.GameState.DRAWN;
import static de.fahsel.chess.enginetournament.model.game.GameState.Reason.FIFTY_MOVES;

@Service
public class FiftyMovesDetector implements GameStateDetector {
    @Override
    public void detectFor(Game game) {
        if (game.getFiftyMovesCount() > 100) {
            game.setState(DRAWN);
            game.setStateReason(FIFTY_MOVES);
        }
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
