package de.fahsel.chess.enginetournament.service.game.engine.movecalculator;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Piece;
import de.fahsel.chess.enginetournament.model.game.PieceType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static de.fahsel.chess.enginetournament.service.game.engine.movecalculator.Direction.*;

@Service
public class LegalBishopMovesCalculator implements LegalPieceMovesCalculator {
    private final DirectionHelper directionHelper;

    public LegalBishopMovesCalculator(DirectionHelper directionHelper) {
        this.directionHelper = directionHelper;
    }

    @Override
    public List<String> get(Game game, Piece piece) {
        return new ArrayList<>(directionHelper.getMoves(
                game,
                piece,
                Condition.FIELD_IS_EMPTY_OR_OPPONENT,
                7,
                NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST
        ));
    }

    @Override
    public PieceType getType() {
        return PieceType.BISHOP;
    }
}
