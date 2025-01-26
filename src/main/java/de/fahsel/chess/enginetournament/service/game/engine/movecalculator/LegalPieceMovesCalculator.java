package de.fahsel.chess.enginetournament.service.game.engine.movecalculator;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Piece;
import de.fahsel.chess.enginetournament.model.game.PieceType;

import java.util.List;

public interface LegalPieceMovesCalculator {
    List<String> get(Game game, Piece piece);
    PieceType getType();
}
