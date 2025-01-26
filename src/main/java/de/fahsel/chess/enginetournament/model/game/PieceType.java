package de.fahsel.chess.enginetournament.model.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PieceType {
    KING("k"),
    QUEEN("q"),
    ROOK("r"),
    KNIGHT("n"),
    BISHOP("b"),
    PAWN("p");

    private final String fenSymbol;
}
