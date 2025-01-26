package de.fahsel.chess.enginetournament.model.gui;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GuiPromotion {
    GuiPiece piece;
    int row;
    int column;
}
