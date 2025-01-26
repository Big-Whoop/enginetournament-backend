package de.fahsel.chess.enginetournament.model.game;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Piece {
    String id;
    PieceType type;
    Side side;
}
