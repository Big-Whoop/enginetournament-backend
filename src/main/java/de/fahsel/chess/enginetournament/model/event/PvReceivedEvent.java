package de.fahsel.chess.enginetournament.model.event;

import de.fahsel.chess.enginetournament.model.engine.Pv;
import de.fahsel.chess.enginetournament.model.game.Game;
import lombok.Builder;
import lombok.Value;

import java.util.concurrent.ConcurrentSkipListMap;

@Value
@Builder
public class PvReceivedEvent implements InGameEvent {
    Game game;
    ConcurrentSkipListMap<Integer, Pv> pvs;
}
