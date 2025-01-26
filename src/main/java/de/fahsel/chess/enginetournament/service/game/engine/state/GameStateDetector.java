package de.fahsel.chess.enginetournament.service.game.engine.state;

import de.fahsel.chess.enginetournament.model.game.Game;
import org.springframework.core.PriorityOrdered;

public interface GameStateDetector extends PriorityOrdered {
    void detectFor(Game game);
}
