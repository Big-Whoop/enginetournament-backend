package de.fahsel.chess.enginetournament.model.gui;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MoveUpdate {
    GuiPiece[][] pieces;
    int whiteScore;
    int blackScore;
    int moveNumber;
    List<GuiMove> moves;
    List<String> captureIds;
    GuiPromotion promotion;
    String rawMove;
}
