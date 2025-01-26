package de.fahsel.chess.enginetournament.service.game;

import de.fahsel.chess.enginetournament.local.ChessClock;
import de.fahsel.chess.enginetournament.model.game.Configuration;
import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Side;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static de.fahsel.chess.enginetournament.model.game.GameState.RUNNING;

@Service
public class GameRegistry {
    private Game game;

    public Game create(Configuration configuration) {
        game = Game
            .builder()
            .moves(new ArrayList<>())
            .engines(new HashMap<>())
                .clock(
                    Map.of(
                        Side.WHITE,
                        ChessClock.builder()
                                .minutes(configuration.getClockMinutes())
                                .seconds(configuration.getClockSeconds())
                                .millisIncrement(configuration.getClockIncrementMillis())
                                .build(),
                        Side.BLACK,
                        ChessClock.builder()
                            .minutes(configuration.getClockMinutes())
                            .seconds(configuration.getClockSeconds())
                            .millisIncrement(configuration.getClockIncrementMillis())
                            .build()
                ))
            .configuration(configuration)
            .state(RUNNING)
            .positionHistory(new ArrayList<>())
            .build();

        return game;
    }

    public Game get() {
        return game;
    }
}
