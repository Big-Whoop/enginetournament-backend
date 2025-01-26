package de.fahsel.chess.enginetournament.service.game.engine.movecalculator;

import de.fahsel.chess.enginetournament.model.game.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LegalPawnMoveCalculator implements LegalPieceMovesCalculator {
    private final DirectionHelper directionHelper;

    public LegalPawnMoveCalculator(DirectionHelper directionHelper) {
        this.directionHelper = directionHelper;
    }

    @Override
    public List<String> get(Game game, Piece piece) {
        Coordinate pieceCoordinate = game.getCoordinate(piece);
        List<String> validMoves = new ArrayList<>();

        if (pieceCoordinate != null) {
            int northRepetitions = repetitions(pieceCoordinate, piece.getSide());

            validMoves.addAll(directionHelper.getMoves(game, piece, Condition.FIELD_IS_EMPTY, northRepetitions, Direction.NORTH));
            validMoves.addAll(directionHelper.getMoves(game, piece, Condition.FIELD_IS_OPPONENT, 1, Direction.NORTH_EAST));
            validMoves.addAll(directionHelper.getMoves(game, piece, Condition.FIELD_IS_OPPONENT, 1, Direction.NORTH_WEST));
        }

        Coordinate enPassant = game.getEnPassant();
        if (enPassant != null) {
            if (enPassant.equals(directionHelper.getTarget(game, piece, Direction.NORTH_EAST, 1))) {
                validMoves.addAll(directionHelper.getMoves(game, piece, Condition.FIELD_IS_EMPTY, 1, Direction.NORTH_EAST));
            } else if (enPassant.equals(directionHelper.getTarget(game, piece, Direction.NORTH_WEST, 1))) {
                validMoves.addAll(directionHelper.getMoves(game, piece, Condition.FIELD_IS_EMPTY, 1, Direction.NORTH_WEST));
            }
        }

        return validMoves;
    }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }

    private int repetitions(Coordinate piecePosition, Side side) {
        int row = piecePosition.getRow();

        return (side.equals(Side.WHITE) && row == 6) || ((side.equals(Side.BLACK) && row == 1)) ? 2 : 1;
    }
}
