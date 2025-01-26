package de.fahsel.chess.enginetournament.service.game.engine.movecalculator;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Piece;
import de.fahsel.chess.enginetournament.model.game.PieceType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static de.fahsel.chess.enginetournament.service.game.engine.movecalculator.Direction.*;

@Service
public class LegalKnightMovesCalculator implements LegalPieceMovesCalculator {
    private final DirectionHelper directionHelper;

    public LegalKnightMovesCalculator(DirectionHelper directionHelper) {
        this.directionHelper = directionHelper;
    }

    @Override
    public List<String> get(Game game, Piece piece) {
        return new ArrayList<>(directionHelper.getMoves(
                game,
                piece,
                Condition.FIELD_IS_EMPTY_OR_OPPONENT,
                1,
                KNIGHT_NEE, KNIGHT_NNE, KNIGHT_NNW, KNIGHT_NWW, KNIGHT_SEE, KNIGHT_SSE, KNIGHT_SSW, KNIGHT_SWW
        ));
    }

    @Override
    public PieceType getType() {
        return PieceType.KNIGHT;
    }
}
