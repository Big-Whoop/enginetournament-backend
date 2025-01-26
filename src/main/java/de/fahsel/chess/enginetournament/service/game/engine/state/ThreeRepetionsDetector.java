package de.fahsel.chess.enginetournament.service.game.engine.state;

import de.fahsel.chess.enginetournament.model.game.Game;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static de.fahsel.chess.enginetournament.model.game.GameState.DRAWN;
import static de.fahsel.chess.enginetournament.model.game.GameState.Reason.REPEATED_POSITION;

@Service
public class ThreeRepetionsDetector implements GameStateDetector {
    @Override
    public void detectFor(Game game) {
        List<String> history = game.getPositionHistory();
        String lastPosition = history.get(history.size() - 1);

        if (Collections.frequency(history, lastPosition) >= 3) {
            game.setState(DRAWN);
            game.setStateReason(REPEATED_POSITION);
        }
    }

    @Override
    public int getOrder() {
        return 20;
    }
}
