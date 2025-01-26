package de.fahsel.chess.enginetournament.model.game;

import de.fahsel.chess.enginetournament.local.ChessClock;
import de.fahsel.chess.enginetournament.model.engine.Engine;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.fahsel.chess.enginetournament.model.game.PieceType.KING;

@Data
@Builder
public class Game {
    private Configuration configuration;
    private Map<Side, Engine> engines;
    private Map<Side, Boolean> queenSideCastlingAllowed;
    private Map<Side, Boolean> kingSideCastlingAllowed;
    private Map<Side, ChessClock> clock;
    private Side side;
    private List<Move> moves;
    private List<String> positionHistory;
    private Piece[][] pieces;
    private Coordinate enPassant;
    private int fiftyMovesCount;
    private int halfMoveCount;
    private GameState state;
    private GameState.Reason stateReason;

    public Piece pieceAt(Coordinate coordinate) {
        return pieces[coordinate.getRow()][coordinate.getColumn()];
    }

    public Piece pieceAt(int row, int column) {
        return pieces[row][column];
    }

    @SuppressWarnings("ConstantConditions")
    public Piece pieceAt(String field) {
        return pieceAt(Coordinate.of(field));
    }

    public void removePiece(Piece piece) {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (pieces[row][column] == piece) {
                    pieces[row][column] = null;
                }
            }
        }
    }

    public Coordinate getCoordinate(Piece piece) {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (pieces[row][column] == piece) {
                    return Coordinate.builder()
                            .row(row)
                            .column(column)
                            .build();
                }
            }
        }

        return null;
    }

    public Coordinate getKingCoordinate(Side side) {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                Piece piece = pieces[row][column];

                if (piece != null && piece.getType().equals(KING) && piece.getSide().equals(side)) {
                    return Coordinate.builder()
                        .row(row)
                        .column(column)
                        .build();
                }
            }
        }

        return null;
    }

    public Game copy() {
        Piece[][] pieces = new Piece[8][8];

        for (int row = 0; row < 8; row ++) {
            for (int column = 0; column < 8; column ++) {
                pieces[row][column] = this.pieceAt(row, column);
            }
        }

        return Game.builder()
            .enPassant(this.getEnPassant())
            .state(GameState.RUNNING)
            .pieces(pieces)
            .halfMoveCount(this.getHalfMoveCount())
            .fiftyMovesCount(this.getFiftyMovesCount())
            .halfMoveCount(this.getHalfMoveCount())
            .side(this.getSide())
            .moves(new ArrayList<>())
            .kingSideCastlingAllowed(new HashMap<>(this.getKingSideCastlingAllowed()))
            .queenSideCastlingAllowed(new HashMap<>(this.getQueenSideCastlingAllowed()))
            .positionHistory(new ArrayList<>())
            .engines(this.engines)
            .build();
    }
}
