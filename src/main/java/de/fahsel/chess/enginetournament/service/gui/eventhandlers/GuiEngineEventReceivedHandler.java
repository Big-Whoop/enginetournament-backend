package de.fahsel.chess.enginetournament.service.gui.eventhandlers;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.event.EngineEventReceivedEvent;
import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Side;
import de.fahsel.chess.enginetournament.model.gui.GuiEngineEventCount;
import de.fahsel.chess.enginetournament.service.game.event.handler.GameEventHandler;
import de.fahsel.chess.enginetournament.service.gui.GuiEventStream;
import org.springframework.stereotype.Service;

@Service
public class GuiEngineEventReceivedHandler implements GameEventHandler<EngineEventReceivedEvent> {
    private final GuiEventStream guiEventStream;

    public GuiEngineEventReceivedHandler(GuiEventStream guiEventStream) {
        this.guiEventStream = guiEventStream;
    }

    @Override
    public void handle(EngineEventReceivedEvent gameEvent) {
        Game game = gameEvent.getGame();

        Engine white = game.getEngines().get(Side.WHITE);
        Engine black = game.getEngines().get(Side.BLACK);

        if (white != null && black != null) {
            guiEventStream.publish("engineEventCount",
                    GuiEngineEventCount.builder()
                            .eventCountWhite(white.getInfo().getTotalEventsReceived().get())
                            .eventCountBlack(black.getInfo().getTotalEventsReceived().get())
                            .build()
            );
        }
    }
}
