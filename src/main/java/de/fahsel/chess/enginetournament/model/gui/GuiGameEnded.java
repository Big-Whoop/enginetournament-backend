package de.fahsel.chess.enginetournament.model.gui;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GuiGameEnded {
    String reason;
    String state;
}
