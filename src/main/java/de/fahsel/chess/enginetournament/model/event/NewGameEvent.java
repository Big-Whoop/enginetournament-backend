package de.fahsel.chess.enginetournament.model.event;

import de.fahsel.chess.enginetournament.model.game.Configuration;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NewGameEvent implements GameEvent {
    Configuration configuration;
}
