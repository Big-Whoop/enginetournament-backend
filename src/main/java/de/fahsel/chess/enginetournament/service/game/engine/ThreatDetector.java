package de.fahsel.chess.enginetournament.service.game.engine;

import de.fahsel.chess.enginetournament.model.game.Coordinate;
import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Side;
import de.fahsel.chess.enginetournament.service.game.engine.movecalculator.ValidMovesCalculator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThreatDetector {
    private final ValidMovesCalculator validMovesCalculator;

    public ThreatDetector(ValidMovesCalculator validMovesCalculator) {
        this.validMovesCalculator = validMovesCalculator;
    }

    public boolean isFieldThreatened(Game game, Side bySide, String field) {
        Game clonedGame = game.copy();

        clonedGame.setSide(bySide);

        List<String> targets = validMovesCalculator.computeValidMoves(clonedGame).stream()
            .map(s -> s.substring(2, 4))
            .collect(Collectors.toList());

        return targets.contains(field);
    }

    public boolean isKingThreatened(Game game, Side side) {
        Coordinate kingCoordinate = game.getKingCoordinate(side);

        if (kingCoordinate == null) {
            return false;
        } else {
            return isFieldThreatened(game, side.opposite(), kingCoordinate.toString());
        }
    }
}
