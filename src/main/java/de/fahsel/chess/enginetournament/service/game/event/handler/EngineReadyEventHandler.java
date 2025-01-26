package de.fahsel.chess.enginetournament.service.game.event.handler;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.event.EngineReadyEvent;
import de.fahsel.chess.enginetournament.model.event.MakeMoveEvent;
import de.fahsel.chess.enginetournament.service.game.event.GameEventStream;
import org.springframework.stereotype.Service;

import static de.fahsel.chess.enginetournament.model.game.Side.BLACK;
import static de.fahsel.chess.enginetournament.model.game.Side.WHITE;

@Service
public class EngineReadyEventHandler implements GameEventHandler<EngineReadyEvent> {
    private final GameEventStream gameEventStream;

    public EngineReadyEventHandler(GameEventStream gameEventStream) {
        this.gameEventStream = gameEventStream;
    }

    @Override
    public void handle(EngineReadyEvent gameEvent) {
        gameEvent.getEngine().setReady(true);

        Engine white = gameEvent.getGame().getEngines().get(WHITE);
        Engine black = gameEvent.getGame().getEngines().get(BLACK);
        
        if (white.getReady() && black.getReady()) {
            MakeMoveEvent makeMoveEvent = MakeMoveEvent.builder()
                    .game(gameEvent.getGame())
                    .build();

            gameEventStream.publish(makeMoveEvent);
        }
    }
}
