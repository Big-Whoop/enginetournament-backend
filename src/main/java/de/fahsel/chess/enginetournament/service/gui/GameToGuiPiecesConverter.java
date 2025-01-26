package de.fahsel.chess.enginetournament.service.gui;

import de.fahsel.chess.enginetournament.model.game.Piece;
import de.fahsel.chess.enginetournament.model.gui.GuiPiece;
import org.springframework.stereotype.Service;

@Service
public class GameToGuiPiecesConverter {
    public GuiPiece[][] convert(Piece[][] pieces) {
        GuiPiece[][] guiPieces = new GuiPiece[8][8];

        for (int row = 0; row < 8; row ++) {
            for (int column = 0; column < 8; column ++) {
                Piece piece = pieces[row][column];
                if (piece == null) {
                    guiPieces[row][column] = null;
                } else {
                    String image = piece.getSide().name().charAt(0) + piece.getType().name().substring(0, 2);

                    guiPieces[row][column] = GuiPiece.builder()
                            .id(piece.getId())
                            .image(image)
                            .name(piece.getType().name())
                            .build();
                }
            }
        }

        return guiPieces;
    }
}
