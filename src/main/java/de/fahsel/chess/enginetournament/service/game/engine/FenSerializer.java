package de.fahsel.chess.enginetournament.service.game.engine;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Piece;
import org.springframework.stereotype.Service;

import static de.fahsel.chess.enginetournament.model.game.Side.BLACK;
import static de.fahsel.chess.enginetournament.model.game.Side.WHITE;

@Service
public class FenSerializer {
    public String serialize(Game game) {
        StringBuilder fenBuilder = new StringBuilder();

        serializeBoard(game, fenBuilder);
        serializeSide(game, fenBuilder);
        serializeCastlings(game, fenBuilder);
        serializeEnPassant(game, fenBuilder);
        serialize50Moves(game, fenBuilder);
        serializeMoves(game, fenBuilder);


        return fenBuilder.toString();
    }

    private void serializeBoard(Game game, StringBuilder fenBuilder) {
        for (int row = 0; row < 8; row ++) {
            int freeSpaces = 0;
            for (int column = 0; column < 8; column ++) {
                if (game.pieceAt(row, column) == null) {
                    freeSpaces ++;
                } else {
                    if (freeSpaces > 0) {
                        fenBuilder.append(freeSpaces);
                    }

                    fenBuilder.append(symbolFor(game.pieceAt(row, column)));
                    freeSpaces = 0;
                }
            }

            if (freeSpaces > 0) {
                fenBuilder.append(freeSpaces);
            }

            if (row != 7) fenBuilder.append("/");
        }
    }

    private void serializeSide(Game game, StringBuilder fenBuilder) {
        fenBuilder
            .append(" ")
            .append(game.getSide().equals(WHITE) ? "w" : "b");
    }

    private void serializeCastlings(Game game, StringBuilder fenBuilder) {
        StringBuilder castlings = new StringBuilder();

        if (game.getKingSideCastlingAllowed().get(WHITE)) {
            castlings.append("K");
        }

        if (game.getQueenSideCastlingAllowed().get(WHITE)) {
            castlings.append("Q");
        }

        if (game.getKingSideCastlingAllowed().get(BLACK)) {
            castlings.append("k");
        }

        if (game.getQueenSideCastlingAllowed().get(BLACK)) {
            castlings.append("q");
        }

        if (castlings.length() == 0) {
            castlings.append("-");
        }

        fenBuilder
            .append(" ")
            .append(castlings.toString());
    }

    private void serializeEnPassant(Game game, StringBuilder fenBuilder) {
        fenBuilder.append(" ");

        if (game.getEnPassant() != null) {
            fenBuilder.append(game.getEnPassant().toString());
        } else {
            fenBuilder.append("-");
        }
    }

    private void serialize50Moves(Game game, StringBuilder fenBuilder) {
        fenBuilder
            .append(" ")
            .append(game.getFiftyMovesCount());
    }

    private void serializeMoves(Game game, StringBuilder fenBuilder) {
        int fullMoves = (int) Math.floor((double) game.getHalfMoveCount() / 2);

        fenBuilder
            .append(" ")
            .append(fullMoves);
    }

    private String symbolFor(Piece piece) {
        return piece.getSide().equals(BLACK) ?
            piece.getType().getFenSymbol() :
            piece.getType().getFenSymbol().toUpperCase();
    }
}
