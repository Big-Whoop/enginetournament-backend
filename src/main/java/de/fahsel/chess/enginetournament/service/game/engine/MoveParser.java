package de.fahsel.chess.enginetournament.service.game.engine;

import de.fahsel.chess.enginetournament.model.engine.Info;
import de.fahsel.chess.enginetournament.model.game.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@SuppressWarnings("ConstantConditions")
@Service
@Log4j2
public class MoveParser {
    private static final Pattern MOVE_FORMAT = Pattern.compile("^[a-h][1-8][a-h][1-8][qrnb]?$");

    public Move parse(Game game, String rawMove) {
        Move move = Move.builder()
                .rawMove(rawMove)
                .build();

        setCoordinates(move, rawMove);
        setPiece(move, game);
        setPromotion(move, rawMove, game);
        setCapturedPiece(move, game);
        setCastling(move, game);
        checkEnPassantCapture(move, game);
        setScore(move, game);
        setWinDrawLoss(move, game);

        return move;
    }

    private void setCoordinates(Move move, String rawMove) {
        move.setFrom(Coordinate.of(rawMove.substring(0, 2)));
        move.setTo(Coordinate.of(rawMove.substring(2, 4)));
    }

    private void setPiece(Move move, Game game) {
        Piece piece = game.pieceAt(move.getFrom());

        move.setPiece(piece);
    }

    private void setPromotion(Move move, String rawMove, Game game) {
        if (rawMove.length() == 5) {
            String id = UUID.randomUUID().toString();

            Piece promotedPiece;
            switch(rawMove.substring(4)) {
                case "q" -> promotedPiece = Piece.builder().id(id).type(PieceType.QUEEN).side(game.getSide()).build();
                case "r" -> promotedPiece = Piece.builder().id(id).type(PieceType.ROOK).side(game.getSide()).build();
                case "n" -> promotedPiece = Piece.builder().id(id).type(PieceType.KNIGHT).side(game.getSide()).build();
                case "b" -> promotedPiece = Piece.builder().id(id).type(PieceType.BISHOP).side(game.getSide()).build();
                default -> promotedPiece = null;
            }

            move.setPromotedPiece(promotedPiece);
        }
    }

    private void setCapturedPiece(Move move, Game game) {
        Piece capturedPiece = game.pieceAt(move.getTo());

        move.setCapturedPiece(capturedPiece);
    }

    private void setCastling(Move move, Game game) {
        if (
                move.getPiece().getType().equals(PieceType.KING) &&
                Math.abs(move.getFrom().getColumn() - move.getTo().getColumn()) == 2
        ) {
            Castling castling = null;

            if (move.getFrom().is("e1") && move.getTo().is("g1")) {
                castling = Castling.builder()
                        .rook(game.pieceAt(Coordinate.of("h1")))
                        .rookFrom(Coordinate.of("h1"))
                        .rookTo(Coordinate.of("f1"))
                        .build();
            } else if (move.getFrom().is("e1") && move.getTo().is("c1")) {
                castling = Castling.builder()
                        .rook(game.pieceAt(Coordinate.of("a1")))
                        .rookFrom(Coordinate.of("a1"))
                        .rookTo(Coordinate.of("d1"))
                        .build();
            } else if (move.getFrom().is("e8") && move.getTo().is("g8")) {
                castling = Castling.builder()
                        .rook(game.pieceAt(Coordinate.of("h8")))
                        .rookFrom(Coordinate.of("h8"))
                        .rookTo(Coordinate.of("f8"))
                        .build();
            } else if (move.getFrom().is("e8") && move.getTo().is("c8")) {
                castling = Castling.builder()
                        .rook(game.pieceAt(Coordinate.of("a8")))
                        .rookFrom(Coordinate.of("a8"))
                        .rookTo(Coordinate.of("d8"))
                        .build();
            }

            move.setCastling(castling);
        }
    }

    private void checkEnPassantCapture(Move move, Game game) {
        Piece piece = move.getPiece();

        if (
                piece.getType().equals(PieceType.PAWN) &&
                move.getCapturedPiece() == null &&
                move.getFrom().getColumn() != move.getTo().getColumn()

        ) {
            int captureRow;
            if (piece.getSide().equals(Side.WHITE)) {
                captureRow = move.getTo().getRow() + 1;
            } else {
                captureRow = move.getTo().getRow() - 1;
            }

            int captureColumn = move.getTo().getColumn();
            Piece capturedPiece = game.pieceAt(Coordinate.builder().column(captureColumn).row(captureRow).build());

            move.setCapturedPiece(capturedPiece);
        }
    }

    private void setScore(Move move, Game game) {
        Info info = game.getEngines().get(game.getSide()).getInfo();

        if (info.getScore() != null) {
            move.setScore(info.getScore());
        }
    }

    private void setWinDrawLoss(Move move, Game game) {
        Info info = game.getEngines().get(game.getSide()).getInfo();

        move.setWinDrawLoss(info.getWinDrawLoss());
    }
}
