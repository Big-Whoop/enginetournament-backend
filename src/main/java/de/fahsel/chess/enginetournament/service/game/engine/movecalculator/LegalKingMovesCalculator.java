package de.fahsel.chess.enginetournament.service.game.engine.movecalculator;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Piece;
import de.fahsel.chess.enginetournament.model.game.PieceType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static de.fahsel.chess.enginetournament.model.game.Side.BLACK;
import static de.fahsel.chess.enginetournament.model.game.Side.WHITE;
import static de.fahsel.chess.enginetournament.service.game.engine.movecalculator.Condition.FIELD_IS_EMPTY;
import static de.fahsel.chess.enginetournament.service.game.engine.movecalculator.Condition.FIELD_IS_EMPTY_OR_OPPONENT;
import static de.fahsel.chess.enginetournament.service.game.engine.movecalculator.Direction.*;

@Service
public class LegalKingMovesCalculator implements LegalPieceMovesCalculator {
    private final DirectionHelper directionHelper;

    public LegalKingMovesCalculator(DirectionHelper directionHelper) {
        this.directionHelper = directionHelper;
    }

    @Override
    public List<String> get(Game game, Piece piece) {
        List<String> moves = new ArrayList<>(directionHelper.getMoves(
                game,
                piece,
                FIELD_IS_EMPTY_OR_OPPONENT,
                1,
                NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST
        ));

        if (game.getKingSideCastlingAllowed().get(game.getSide())) {
            if (
                (game.getSide().equals(WHITE) && game.pieceAt("f1") == null) ||
                (game.getSide().equals(BLACK) && game.pieceAt("f8") == null)
            ) {
                moves.addAll(directionHelper.getMoves(game, piece, FIELD_IS_EMPTY, 1, KING_SIDE_CASTLING));
            }
        } else if (game.getQueenSideCastlingAllowed().get(game.getSide())) {
            if (
                (game.getSide().equals(WHITE) && game.pieceAt("d1") == null) ||
                (game.getSide().equals(BLACK) && game.pieceAt("d8") == null)
            ) {
                moves.addAll(directionHelper.getMoves(game, piece, FIELD_IS_EMPTY, 1, QUEEN_SIDE_CASTLING));
            }
        }

        return moves;
    }

    @Override
    public PieceType getType() {
        return PieceType.KING;
    }
}
