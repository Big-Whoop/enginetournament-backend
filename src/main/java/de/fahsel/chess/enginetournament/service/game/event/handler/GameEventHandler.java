package de.fahsel.chess.enginetournament.service.game.event.handler;

import de.fahsel.chess.enginetournament.model.event.GameEvent;

public interface GameEventHandler<T extends GameEvent> {
    void handle(T gameEvent);
}
