package de.fahsel.chess.enginetournament.service.gui.eventhandlers;

import de.fahsel.chess.enginetournament.model.event.GameReadyEvent;
import de.fahsel.chess.enginetournament.service.game.event.GameEventStream;
import de.fahsel.chess.enginetournament.service.game.event.handler.GameEventHandler;
import de.fahsel.chess.enginetournament.service.gui.event.GuiRefreshEvent;
import org.springframework.stereotype.Service;

@Service
public class GuiGameReadyEventHandler implements GameEventHandler<GameReadyEvent> {
    private final GameEventStream gameEventStream;

    public GuiGameReadyEventHandler(GameEventStream gameEventStream) {
        this.gameEventStream = gameEventStream;
    }


    @Override
    public void handle(GameReadyEvent gameEvent) {
        GuiRefreshEvent guiRefreshEvent = GuiRefreshEvent.builder()
                .newGame(true)
                .build();

        gameEventStream.publish(guiRefreshEvent);
    }
}
