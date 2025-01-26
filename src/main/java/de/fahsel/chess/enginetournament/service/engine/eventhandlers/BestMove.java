package de.fahsel.chess.enginetournament.service.engine.eventhandlers;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.event.MadeMoveEvent;
import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Move;
import de.fahsel.chess.enginetournament.service.game.engine.MoveExecutor;
import de.fahsel.chess.enginetournament.service.game.engine.MoveParser;
import de.fahsel.chess.enginetournament.service.game.event.GameEventStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Log4j2
public class BestMove implements EngineEventHandler {
    private final GameEventStream gameEventStream;
    private final MoveParser moveParser;
    private final MoveExecutor moveExecutor;

    public BestMove(
        GameEventStream gameEventStream,
        MoveParser moveParser, MoveExecutor moveExecutor
    ) {
        this.gameEventStream = gameEventStream;
        this.moveParser = moveParser;
        this.moveExecutor = moveExecutor;
    }

    @Override
    public void handle(String lineFromEngine, Engine engine) {
        Game game = engine.getGame();
        String[] tokens = lineFromEngine.split(" ");
        Move move = moveParser.parse(game, tokens.length > 1 ? tokens[1] : "none");

        game.getClock().get(game.getSide()).stop();

        moveExecutor.execute(game, move);

        MadeMoveEvent madeMoveEvent = MadeMoveEvent.builder()
                .move(move)
                .game(game)
                .build();

        gameEventStream.publish(madeMoveEvent, Duration.ofMillis(250));
    }

    @Override
    public String command() {
        return "bestmove";
    }
}
