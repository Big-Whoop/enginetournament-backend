package de.fahsel.chess.enginetournament.service.game.engine;

import de.fahsel.chess.enginetournament.model.game.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static de.fahsel.chess.enginetournament.model.game.Side.BLACK;

@Service
public class FenInitializer {
    public void initialize(Game game, String fen) {
        String[] groups = fen.split(" ");

        setupPieces(game, groups[0]);
        setupSide(game, groups[1]);
        setupCastling(game, groups[2]);
        setupEnPassant(game, groups[3]);
        setupFiftyMoves(game, groups[4]);
        setupMoveCount(game, groups[5]);
    }

    private void setupPieces(Game game, String group) {
        final Piece[][] board = new Piece[8][8];

        String[] rows = group.split("/");

        for (int iRow = 0; iRow < 8; iRow++) {
            String row = rows[iRow];
            int iColumn = 0;

            for (int i = 0; i < row.length(); i++) {
                String character = String.valueOf(row.charAt(i));
                if (StringUtils.isNumeric(character)) {
                    iColumn = iColumn + Integer.parseInt(character);
                } else {
                    board[iRow][iColumn] = createPiece(character);
                    iColumn ++;
                }
            }
        }

        game.setPieces(board);
    }

    private void setupCastling(Game game, String group) {
        Map<Side, Boolean> kingSideCastlingAllowed = new HashMap<>();
        Map<Side, Boolean> queenSideCastlingAllowed = new HashMap<>();

        kingSideCastlingAllowed.put(Side.WHITE, group.contains("K"));
        queenSideCastlingAllowed.put(Side.WHITE, group.contains("Q"));
        kingSideCastlingAllowed.put(BLACK, group.contains("k"));
        queenSideCastlingAllowed.put(BLACK, group.contains("q"));

        game.setKingSideCastlingAllowed(kingSideCastlingAllowed);
        game.setQueenSideCastlingAllowed(queenSideCastlingAllowed);
    }

    private void setupEnPassant(Game game, String group) {
        game.setEnPassant(Coordinate.of(group));
    }

    private void setupSide(Game game, String group) {
        game.setSide(group.equals("w") ? Side.WHITE : BLACK);
    }

    private void setupFiftyMoves(Game game, String group) {
        game.setFiftyMovesCount(Integer.parseInt(group));
    }

    private void setupMoveCount(Game game, String group) {
        int nextFullMoveCount = Integer.parseInt(group) - 1;
        int halfMoveCount = nextFullMoveCount * 2;

        if (game.getSide().equals(BLACK)) {
            halfMoveCount ++;
        }

        game.setHalfMoveCount(halfMoveCount);
    }

    private Piece createPiece(String character) {
        Piece piece;
        String id = UUID.randomUUID().toString();

        switch (character) {
            case "k" -> piece = Piece.builder().id(id).type(PieceType.KING).side(BLACK).build();
            case "q" -> piece = Piece.builder().id(id).type(PieceType.QUEEN).side(BLACK).build();
            case "r" -> piece = Piece.builder().id(id).type(PieceType.ROOK).side(BLACK).build();
            case "n" -> piece = Piece.builder().id(id).type(PieceType.KNIGHT).side(BLACK).build();
            case "b" -> piece = Piece.builder().id(id).type(PieceType.BISHOP).side(BLACK).build();
            case "p" -> piece = Piece.builder().id(id).type(PieceType.PAWN).side(BLACK).build();
            case "K" -> piece = Piece.builder().id(id).type(PieceType.KING).side(Side.WHITE).build();
            case "Q" -> piece = Piece.builder().id(id).type(PieceType.QUEEN).side(Side.WHITE).build();
            case "R" -> piece = Piece.builder().id(id).type(PieceType.ROOK).side(Side.WHITE).build();
            case "N" -> piece = Piece.builder().id(id).type(PieceType.KNIGHT).side(Side.WHITE).build();
            case "B" -> piece = Piece.builder().id(id).type(PieceType.BISHOP).side(Side.WHITE).build();
            case "P" -> piece = Piece.builder().id(id).type(PieceType.PAWN).side(Side.WHITE).build();
            default -> piece = null;
        }

        return piece;
    }
}
