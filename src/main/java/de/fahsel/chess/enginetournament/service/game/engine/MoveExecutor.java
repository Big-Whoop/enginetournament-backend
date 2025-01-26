package de.fahsel.chess.enginetournament.service.game.engine;

import de.fahsel.chess.enginetournament.model.game.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.fahsel.chess.enginetournament.model.game.Side.BLACK;

@Service
@Log4j2
public class MoveExecutor {
    private final FenSerializer fenSerializer;

    public MoveExecutor(FenSerializer fenSerializer) {
        this.fenSerializer = fenSerializer;
    }

    public void execute(Game game, Move move) {
        capture(game, move);
        move(game, move.getFrom(), move.getTo());
        castle(game, move);
        promote(game, move);
        markEnPassantField(game, move);
        disallowCastling(game, move);
        addMoveToHistory(game, move);
        addPositionToHistory(game);
        increaseMoveCount(game);
        increase50MovesCount(game, move);
        switchSides(game);
        // draw(game, move);
    }

    private void move(Game game, Coordinate from, Coordinate to) {
        Piece[][] pieces = game.getPieces();

        pieces[to.getRow()][to.getColumn()] = pieces[from.getRow()][from.getColumn()];
        pieces[from.getRow()][from.getColumn()] = null;
    }

    private void capture(Game game, Move move) {
        if (move.getCapturedPiece() != null) {
            game.removePiece(move.getCapturedPiece());
        }
    }

    private void castle(Game game, Move move) {
        if (move.getCastling() != null) {
            Castling castling = move.getCastling();

            move(game, castling.getRookFrom(), castling.getRookTo());

            game.getKingSideCastlingAllowed().put(game.getSide(), false);
            game.getQueenSideCastlingAllowed().put(game.getSide(), false);
        }
    }

    private void promote(Game game, Move move) {
        if (move.getPromotedPiece() != null) {
            game.getPieces()[move.getTo().getRow()][move.getTo().getColumn()] = move.getPromotedPiece();
        }
    }

    private void markEnPassantField(Game game, Move move) {
        if (
            move.getPiece().getType().equals(PieceType.PAWN) &&
            Math.abs(move.getFrom().getRow() - move.getTo().getRow()) == 2
        ) {
            int enPassantRow = (move.getTo().getRow() + move.getFrom().getRow()) / 2;

            Coordinate enPassant = Coordinate.builder().row(enPassantRow).column(move.getTo().getColumn()).build();

            game.setEnPassant(enPassant);
        } else {
            game.setEnPassant(null);
        }
    }

    private void disallowCastling(Game game, Move move) {
        if (move.getPiece().getType().equals(PieceType.KING)) {
            game.getKingSideCastlingAllowed().put(game.getSide(), false);
            game.getQueenSideCastlingAllowed().put(game.getSide(), false);
        } else {
            String moveNotation = move.getFrom().toString();

            if (
                move.getPiece().getType().equals(PieceType.ROOK) &&
                game.getSide().equals(Side.WHITE) &&
                List.of("a1", "h1").contains(moveNotation)
            ) {
                game.getQueenSideCastlingAllowed().put(game.getSide(), false);
            } else if (
                move.getPiece().getType().equals(PieceType.ROOK) &&
                game.getSide().equals(Side.WHITE) &&
                List.of("a8", "h8").contains(moveNotation)
            ) {
                game.getKingSideCastlingAllowed().put(game.getSide(), false);
            }
        }
    }

    private void addMoveToHistory(Game game, Move move) {
        game.getMoves().add(move);
    }

    private void addPositionToHistory(Game game) {
        String fen = fenSerializer.serialize(game);
        String position = fen.substring(0, fen.indexOf(" "));

        game.getPositionHistory().add(position);
    }

    private void increaseMoveCount(Game game) {
        game.setHalfMoveCount(game.getHalfMoveCount() + 1);
    }

    private void increase50MovesCount(Game game, Move move) {
        if (move.getPiece().getType().equals(PieceType.PAWN) || move.getCapturedPiece() != null) {
            game.setFiftyMovesCount(0);
        } else {
            game.setFiftyMovesCount(game.getFiftyMovesCount() + 1);
        }
    }

    private void switchSides(Game game) {
        game.setSide(game.getSide().equals(Side.WHITE) ? BLACK : Side.WHITE);
    }

    private void draw(Game game, Move move) {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        for (int row = 0; row < 8; row ++) {
            for (int column = 0; column < 8; column ++) {
                Piece piece = game.getPieces()[row][column];

                if (piece == null) {
                    System.out.print("[   ] ");
                } else {
                    System.out.print("[" + piece.getSide().name().substring(0, 1));
                    System.out.print(piece.getType().name().substring(0, 2) + "] ");
                }
            }
            System.out.println();
            System.out.println();
        }
    }
}
