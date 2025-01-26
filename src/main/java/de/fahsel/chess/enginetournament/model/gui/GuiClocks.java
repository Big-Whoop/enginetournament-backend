package de.fahsel.chess.enginetournament.model.gui;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GuiClocks {
    String white;
    String black;
}
