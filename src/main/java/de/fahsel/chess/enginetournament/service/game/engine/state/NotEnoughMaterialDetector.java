package de.fahsel.chess.enginetournament.service.game.engine.state;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.GameState;
import de.fahsel.chess.enginetournament.model.game.Piece;
import de.fahsel.chess.enginetournament.model.game.Side;
import org.springframework.stereotype.Service;

import static de.fahsel.chess.enginetournament.model.game.GameState.DRAWN;

@Service
public class NotEnoughMaterialDetector implements GameStateDetector {
    @Override
    public void detectFor(Game game) {
        if (insufficientMaterial(game, Side.WHITE) && insufficientMaterial(game, Side.BLACK)) {
            game.setState(DRAWN);
            game.setStateReason(GameState.Reason.NOT_ENOUGH_PIECES);
        }
    }

    @Override
    public int getOrder() {
        return 30;
    }

    private boolean insufficientMaterial(Game game, Side side) {
        int heavyPieces = 0;
        int lightPieces = 0;
        int pawns = 0;

        for (int row = 0; row < 8; row ++) {
            for (int column = 0; column < 8; column ++) {
                Piece piece = game.pieceAt(row, column);

                if (piece != null && piece.getSide().equals(side)) {
                    switch (piece.getType()) {
                        case QUEEN, ROOK -> heavyPieces ++;
                        case BISHOP, KNIGHT -> lightPieces ++;
                        case PAWN -> pawns ++;
                    }
                }
            }
        }

        return pawns < 1 && heavyPieces < 1 && lightPieces < 2;
    }
}
