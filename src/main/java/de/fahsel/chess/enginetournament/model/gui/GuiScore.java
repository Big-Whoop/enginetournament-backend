package de.fahsel.chess.enginetournament.model.gui;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GuiScore {
    int index;
    int white;
    int black;
}
