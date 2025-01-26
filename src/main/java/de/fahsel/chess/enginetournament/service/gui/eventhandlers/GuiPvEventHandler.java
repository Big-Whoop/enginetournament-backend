package de.fahsel.chess.enginetournament.service.gui.eventhandlers;

import de.fahsel.chess.enginetournament.model.event.PvReceivedEvent;
import de.fahsel.chess.enginetournament.service.game.event.handler.GameEventHandler;
import de.fahsel.chess.enginetournament.service.gui.GuiEventStream;
import org.springframework.stereotype.Service;

@Service
public class GuiPvEventHandler implements GameEventHandler<PvReceivedEvent> {
    private final GuiEventStream guiEventStream;

    public GuiPvEventHandler(GuiEventStream guiEventStream) {
        this.guiEventStream = guiEventStream;
    }

    @Override
    public void handle(PvReceivedEvent gameEvent) {
        guiEventStream.publish("pv", gameEvent.getPvs().values());
    }
}
