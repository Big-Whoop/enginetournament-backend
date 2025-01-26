package de.fahsel.chess.enginetournament.model.game;

import de.fahsel.chess.enginetournament.model.engine.WinDrawLoss;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Move {
    String rawMove;
    Coordinate from;
    Coordinate to;
    Piece piece;
    Piece capturedPiece;
    Piece promotedPiece;
    Castling castling;
    int score;
    WinDrawLoss winDrawLoss;
}
