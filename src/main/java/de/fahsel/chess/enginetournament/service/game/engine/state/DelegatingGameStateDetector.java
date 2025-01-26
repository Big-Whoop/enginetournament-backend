package de.fahsel.chess.enginetournament.service.game.engine.state;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.GameState;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class DelegatingGameStateDetector {
    private final Map<Integer, GameStateDetector> gameStateDetectors = new TreeMap<>();

    public DelegatingGameStateDetector(List<GameStateDetector> gameStateDetectors) {
        gameStateDetectors.forEach(
            gameStateDetector ->  this.gameStateDetectors.put(gameStateDetector.getOrder(), gameStateDetector)
        );
    }

    public void detectFor(Game game) {
        for (GameStateDetector gameStateDetector: gameStateDetectors.values()) {
            gameStateDetector.detectFor(game);

            if (!game.getState().equals(GameState.RUNNING)) {
                break;
            }
        }
    }
}
