package de.fahsel.chess.enginetournament.model.game;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Castling {
    Coordinate rookFrom;
    Coordinate rookTo;
    Piece rook;
}
