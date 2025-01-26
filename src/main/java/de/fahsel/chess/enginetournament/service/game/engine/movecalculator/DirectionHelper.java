package de.fahsel.chess.enginetournament.service.game.engine.movecalculator;

import de.fahsel.chess.enginetournament.model.game.Coordinate;
import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Piece;
import de.fahsel.chess.enginetournament.model.game.Side;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static de.fahsel.chess.enginetournament.service.game.engine.movecalculator.Direction.KING_SIDE_CASTLING;
import static de.fahsel.chess.enginetournament.service.game.engine.movecalculator.Direction.QUEEN_SIDE_CASTLING;

@Service
@Log4j2
public class DirectionHelper {
    public List<String> getMoves(Game game, Piece piece, Condition condition, int repetitions, Direction... directions) {
        List<String> moves = new ArrayList<>();

        for (Direction direction: directions) {
             for (int i = 1; i <= repetitions; i ++) {
                if (!addMove(moves, game, piece, condition, i, direction)) {
                    break;
                }
            }
        }

        return moves;
    }

    public Coordinate getTarget(Game game, Piece fromPiece, Direction direction, int repetitions) {
        Coordinate fromCoordinate = game.getCoordinate(fromPiece);

        int multiplicator = fromPiece.getSide().equals(Side.WHITE) ||
            List.of(QUEEN_SIDE_CASTLING, KING_SIDE_CASTLING).contains(direction) ? 1 : -1;

        int toRow = fromCoordinate.getRow() + (direction.getRows() * repetitions * multiplicator);
        int toColumn = fromCoordinate.getColumn() + (direction.getColumns() * repetitions * multiplicator);

        if (toRow < 0 || toColumn < 0 || toRow > 7 || toColumn > 7) {
            return null;
        }

        return Coordinate.builder().row(toRow).column(toColumn).build();
    }

    private boolean addMove(List<String> moves, Game game, Piece piece, Condition condition, int repetitions, Direction direction) {
        Coordinate target = getTarget(game, piece, direction, repetitions);

        if (target == null) {
            return false;
        }

        boolean isEmpty = isEmpty(game, piece, direction, repetitions);
        boolean isOpponent = isOpponent(game, piece, direction, repetitions);

        if (
                (condition.equals(Condition.FIELD_IS_EMPTY) && isEmpty) ||
                (condition.equals(Condition.FIELD_IS_OPPONENT) && isOpponent) ||
                (condition.equals(Condition.FIELD_IS_EMPTY_OR_OPPONENT) && (isOpponent || isEmpty))
        ) {
            moves.add(createMove(game, piece, target));
        }

        return isEmpty;
    }

    private String createMove(Game game, Piece piece, Coordinate target) {
        Coordinate from = game.getCoordinate(piece);

        return from.toString() + target.toString();
    }

    private boolean isEmpty(Game game, Piece fromPiece, Direction direction, int repetitions) {
        Piece pieceOnTarget = pieceOnTarget(game, fromPiece, direction, repetitions);

        return pieceOnTarget == null;
    }

    private boolean isOpponent(Game game, Piece fromPiece, Direction direction, int repetitions) {
        Piece pieceOnTarget = pieceOnTarget(game, fromPiece, direction, repetitions);

        return pieceOnTarget != null && !pieceOnTarget.getSide().equals(fromPiece.getSide());
    }

    private Piece pieceOnTarget(Game game, Piece fromPiece, Direction direction, int repetitions) {
        Coordinate target = getTarget(game, fromPiece, direction, repetitions);

        if (target == null) {
            return null;
        }

        return game.pieceAt(target);
    }
}
