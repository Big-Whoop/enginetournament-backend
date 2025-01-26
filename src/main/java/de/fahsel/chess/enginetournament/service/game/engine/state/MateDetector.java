package de.fahsel.chess.enginetournament.service.game.engine.state;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.GameState;
import de.fahsel.chess.enginetournament.model.game.Move;
import de.fahsel.chess.enginetournament.service.game.engine.MoveExecutor;
import de.fahsel.chess.enginetournament.service.game.engine.MoveParser;
import de.fahsel.chess.enginetournament.service.game.engine.ThreatDetector;
import de.fahsel.chess.enginetournament.service.game.engine.movecalculator.ValidMovesCalculator;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.fahsel.chess.enginetournament.model.game.GameState.BLACK_WINS;
import static de.fahsel.chess.enginetournament.model.game.GameState.WHITE_WINS;
import static de.fahsel.chess.enginetournament.model.game.Side.WHITE;

@Service
@Log4j2
public class MateDetector implements GameStateDetector {
    private final ValidMovesCalculator validMovesCalculator;
    private final ThreatDetector threatDetector;
    private final MoveParser moveParser;
    private final MoveExecutor moveExecutor;

    public MateDetector(
        ValidMovesCalculator validMovesCalculator,
        ThreatDetector threatDetector,
        MoveParser moveParser,
        MoveExecutor moveExecutor
    ) {
        this.validMovesCalculator = validMovesCalculator;
        this.threatDetector = threatDetector;
        this.moveParser = moveParser;
        this.moveExecutor = moveExecutor;
    }

    @Override
    public void detectFor(Game game) {
        List<String> validMoves = validMovesCalculator.computeValidMoves(game);

        boolean isCheck = threatDetector.isKingThreatened(game, game.getSide());
        boolean legalMoveFound = false;
        for (String validMove: validMoves) {
            Game clonedGame = game.copy();

            Move parsedValidMove = moveParser.parse(clonedGame, validMove);

            moveExecutor.execute(clonedGame, parsedValidMove);

            if (!threatDetector.isKingThreatened(clonedGame, game.getSide())) {
                legalMoveFound = true;
            }
        }

        if (!legalMoveFound) {
            if (isCheck) {
                game.setState(game.getSide().equals(WHITE) ? BLACK_WINS : WHITE_WINS);
                game.setStateReason(GameState.Reason.MATE);
            } else {
                game.setState(GameState.DRAWN);
                game.setStateReason(GameState.Reason.STALEMATE);
            }
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
