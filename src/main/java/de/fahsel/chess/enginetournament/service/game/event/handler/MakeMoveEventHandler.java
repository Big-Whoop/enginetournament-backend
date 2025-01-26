package de.fahsel.chess.enginetournament.service.game.event.handler;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.event.MakeMoveEvent;
import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Move;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static de.fahsel.chess.enginetournament.model.game.Side.BLACK;
import static de.fahsel.chess.enginetournament.model.game.Side.WHITE;

@Service
public class MakeMoveEventHandler implements GameEventHandler<MakeMoveEvent> {
    private static final String DEFAULT_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Override
    public void handle(MakeMoveEvent gameEvent) {
        Game game = gameEvent.getGame();
        Engine engine = game.getEngines().get(game.getSide());

        List<String> moves = game.getMoves().stream()
            .map(Move::getRawMove)
            .collect(Collectors.toList());

        String moveString = String.join(" ", moves);

        String positionCommand = game.getConfiguration().getFen().equals(DEFAULT_FEN)
            ? "position startpos"
            : "position fen " + game.getConfiguration().getFen();

        if (moveString.isBlank()) {
            engine.getCommandSink().next(positionCommand);
        } else {
            engine.getCommandSink().next(positionCommand + " moves " + moveString);
        }

        engine.getInfo().getPvs().clear();

        if (game.getConfiguration().getClockIncrementMillis() > 0) {
            engine.getCommandSink().next(
                "go wtime " + game.getClock().get(WHITE).millisRemaining() + " " +
                    "btime " + game.getClock().get(BLACK).millisRemaining() + " " +
                    "winc " + (game.getConfiguration().getClockIncrementMillis()) + " " +
                    "binc " + (game.getConfiguration().getClockIncrementMillis())
            );
        } else {
            engine.getCommandSink().next(
                "go wtime " + game.getClock().get(WHITE).millisRemaining() + " " +
                    "btime " + game.getClock().get(BLACK).millisRemaining() + " "
            );
        }

        game.getClock().get(game.getSide()).start();
    }
}
