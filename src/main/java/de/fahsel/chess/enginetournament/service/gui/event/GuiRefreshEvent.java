package de.fahsel.chess.enginetournament.service.gui.event;

import de.fahsel.chess.enginetournament.model.event.GameEvent;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GuiRefreshEvent implements GameEvent {
    boolean newGame;
}
