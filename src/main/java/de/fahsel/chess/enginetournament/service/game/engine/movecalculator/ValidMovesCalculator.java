package de.fahsel.chess.enginetournament.service.game.engine.movecalculator;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Piece;
import de.fahsel.chess.enginetournament.model.game.PieceType;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidMovesCalculator {
    private Map<PieceType, LegalPieceMovesCalculator> legalPieceMovesAnalyzers = new HashMap<>();

    public ValidMovesCalculator(List<LegalPieceMovesCalculator> legalPieceMovesCalculators) {
        legalPieceMovesCalculators.forEach(
                legalPieceMovesAnalyzer -> this.legalPieceMovesAnalyzers.put(
                legalPieceMovesAnalyzer.getType(),
                        legalPieceMovesAnalyzer
            )
        );
    }

    @SneakyThrows
    public List<String> computeValidMoves(Game game) {
        List<String> validMoves = new ArrayList<>();

        for (int row = 0; row < 8; row ++) {
            for (int column = 0; column < 8; column ++) {
                Piece piece = game.pieceAt(row, column);

                if (piece != null && piece.getSide().equals(game.getSide())) {
                    LegalPieceMovesCalculator legalPieceMovesCalculator = legalPieceMovesAnalyzers.get(piece.getType());

                    validMoves.addAll(legalPieceMovesCalculator.get(game, piece));
                }
            }
        }

        return validMoves;
    }
}
