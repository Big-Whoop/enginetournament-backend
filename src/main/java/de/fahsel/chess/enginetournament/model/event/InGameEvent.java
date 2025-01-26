package de.fahsel.chess.enginetournament.model.event;

import de.fahsel.chess.enginetournament.model.game.Game;

public interface    InGameEvent extends GameEvent {
    Game getGame();
}
