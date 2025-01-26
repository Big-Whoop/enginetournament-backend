package de.fahsel.chess.enginetournament.service.game.engine.movecalculator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Direction {
    NORTH(-1,0),
    NORTH_EAST(-1, 1),
    EAST(0, 1),
    SOUTH_EAST(1, 1),
    SOUTH(1, 0),
    SOUTH_WEST(1, -1),
    WEST(0, -1),
    NORTH_WEST(-1, -1),

    KNIGHT_NNE(-2, 1),
    KNIGHT_NEE(-1, 2),
    KNIGHT_SEE(1, 2),
    KNIGHT_SSE(2, 1),
    KNIGHT_SSW(2, -1),
    KNIGHT_SWW(1, -2),
    KNIGHT_NWW(-1, -2),
    KNIGHT_NNW(-2, -1),

    KING_SIDE_CASTLING(0, 2),
    QUEEN_SIDE_CASTLING(0, -2);

    private final int rows;
    private final int columns;
}
