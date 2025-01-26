package de.fahsel.chess.enginetournament.service.game.event.handler;

import de.fahsel.chess.enginetournament.model.event.GameEndedEvent;
import de.fahsel.chess.enginetournament.model.event.MadeMoveEvent;
import de.fahsel.chess.enginetournament.model.event.MakeMoveEvent;
import de.fahsel.chess.enginetournament.model.event.UnregisterGameEvent;
import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.GameState;
import de.fahsel.chess.enginetournament.service.game.engine.state.DelegatingGameStateDetector;
import de.fahsel.chess.enginetournament.service.game.event.GameEventStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static de.fahsel.chess.enginetournament.model.game.GameState.BLACK_WINS;
import static de.fahsel.chess.enginetournament.model.game.GameState.WHITE_WINS;

@Service
@Log4j2
public class MadeMoveEventHandler implements GameEventHandler<MadeMoveEvent> {
    private final GameEventStream gameEventStream;
    private final DelegatingGameStateDetector gameStateDetector;

    public MadeMoveEventHandler(GameEventStream gameEventStream, DelegatingGameStateDetector gameStateDetector) {
        this.gameEventStream = gameEventStream;
        this.gameStateDetector = gameStateDetector;
    }

    @Override
    public void handle(MadeMoveEvent gameEvent) {
        Game game = gameEvent.getGame();

        gameStateDetector.detectFor(game);

        if (game.getState().equals(GameState.RUNNING)) {
            MakeMoveEvent makeMoveEvent = MakeMoveEvent.builder()
                .game(game)
                .build();

            gameEventStream.publish(makeMoveEvent);
        } else {
            log.info("Game ended in state " + game.getState() + " due to " + game.getStateReason());

            double whiteScore;
            double blackScore;

            if (game.getState().equals(WHITE_WINS)) {
                whiteScore = 1;
                blackScore = 0;
            } else if (game.getState().equals(BLACK_WINS)) {
                whiteScore = 0;
                blackScore = 1;
            } else {
                whiteScore = .5;
                blackScore = .5;
            }

            GameEndedEvent gameEndedEvent = GameEndedEvent.builder()
                .game(game)
                .whiteScore(whiteScore)
                .blackScore(blackScore)
                .build();

            UnregisterGameEvent unregisterGameEvent = UnregisterGameEvent.builder()
                .game(game)
                .build();

            gameEventStream.publish(unregisterGameEvent);
            gameEventStream.publish(gameEndedEvent);
        }
    }
}
