package de.fahsel.chess.enginetournament.model.gui;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GuiEngineEventCount {
    int eventCountWhite;
    int eventCountBlack;
}
