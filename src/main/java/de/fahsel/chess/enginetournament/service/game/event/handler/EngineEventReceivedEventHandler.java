package de.fahsel.chess.enginetournament.service.game.event.handler;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.event.EngineEventReceivedEvent;
import de.fahsel.chess.enginetournament.model.game.Game;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EngineEventReceivedEventHandler implements GameEventHandler<EngineEventReceivedEvent> {
    @Override
    public void handle(EngineEventReceivedEvent gameEvent) {
        Game game = gameEvent.getGame();

        Engine engine = game.getEngines().get(game.getSide());

        if (engine != null) {
            engine.getInfo().getTotalEventsReceived().incrementAndGet();
        }
    }
}
