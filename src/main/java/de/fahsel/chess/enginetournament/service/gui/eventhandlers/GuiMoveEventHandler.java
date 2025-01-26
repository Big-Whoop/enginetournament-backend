package de.fahsel.chess.enginetournament.service.gui.eventhandlers;

import de.fahsel.chess.enginetournament.model.engine.Pv;
import de.fahsel.chess.enginetournament.model.event.MadeMoveEvent;
import de.fahsel.chess.enginetournament.model.game.*;
import de.fahsel.chess.enginetournament.model.gui.GuiMove;
import de.fahsel.chess.enginetournament.model.gui.MoveUpdate;
import de.fahsel.chess.enginetournament.model.gui.GuiPiece;
import de.fahsel.chess.enginetournament.model.gui.GuiPromotion;
import de.fahsel.chess.enginetournament.service.game.event.handler.GameEventHandler;
import de.fahsel.chess.enginetournament.service.gui.GameToGuiPiecesConverter;
import de.fahsel.chess.enginetournament.service.gui.GuiEventStream;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class GuiMoveEventHandler implements GameEventHandler<MadeMoveEvent> {
    private final GuiEventStream guiEventStream;
    private final GameToGuiPiecesConverter gameToGuiPiecesConverter;

    public GuiMoveEventHandler(GuiEventStream guiEventStream, GameToGuiPiecesConverter gameToGuiPiecesConverter) {
        this.guiEventStream = guiEventStream;
        this.gameToGuiPiecesConverter = gameToGuiPiecesConverter;
    }

    @Override
    public void handle(MadeMoveEvent gameEvent) {
        Move move = gameEvent.getMove();
        Game game = gameEvent.getGame();

        List<GuiMove> guiMoveList = new ArrayList<>();
        List<String> captureIds = new ArrayList<>();

        processMovingPiece(move, guiMoveList);
        processCapture(move, captureIds);
        processCastling(move, guiMoveList);
        GuiPromotion guiPromotion = processPromotion(move, captureIds);

        int whiteScore = getScore(game, Side.WHITE);
        int blackScore = getScore(game, Side.BLACK);

        MoveUpdate moveUpdate = MoveUpdate.builder()
                .moves(guiMoveList)
                .captureIds(captureIds)
                .promotion(guiPromotion)
                .rawMove(move.getRawMove())
                .moveNumber(game.getHalfMoveCount() - 1)
                .whiteScore(limit(whiteScore))
                .blackScore(- limit(blackScore))
                .pieces(gameToGuiPiecesConverter.convert(game.getPieces()))
                .build();

        guiEventStream.publish("move", moveUpdate);
    }

    private void processMovingPiece(Move move, List<GuiMove> guiMoveList) {
        guiMoveList.add(
                GuiMove.builder()
                        .pieceId(move.getPiece().getId())
                        .row(move.getTo().getRow())
                        .column(move.getTo().getColumn())
                        .build()
        );
    }

    private void processCapture(Move move, List<String> captureIds) {
        if (move.getCapturedPiece() != null) {
            captureIds.add(move.getCapturedPiece().getId());
        }
    }

    private void processCastling(Move move, List<GuiMove> guiMoveList) {
        Castling castling = move.getCastling();
        if (castling != null) {
            guiMoveList.add(
                    GuiMove.builder()
                            .pieceId(castling.getRook().getId())
                            .row(castling.getRookTo().getRow())
                            .column(castling.getRookTo().getColumn())
                            .build()
            );
        }
    }

    private GuiPromotion processPromotion(Move move, List<String> captureIds) {
        Piece promotedPiece = move.getPromotedPiece();
        GuiPromotion guiPromotion = null;
        if (promotedPiece != null) {
            String image = promotedPiece.getSide().name().substring(0, 1) + promotedPiece.getType().name().substring(0, 2);

            GuiPiece promotedGuiPiece = GuiPiece.builder()
                    .id(promotedPiece.getId())
                    .image(image)
                    .name(promotedPiece.getType().name())
                    .build();

            guiPromotion = GuiPromotion.builder()
                    .piece(promotedGuiPiece)
                    .row(move.getTo().getRow())
                    .column(move.getTo().getColumn())
                    .build();

            captureIds.add(move.getPiece().getId());
        }
        return guiPromotion;
    }

    private int limit(int value) {
        return Math.max(Math.min(value, 1000), -1000);
    }

    private int getScore(Game game, Side white) {
        Collection<Pv> whitePv = game.getEngines().get(white).getInfo().getPvs().values();
        return whitePv.size() > 0 ? whitePv.stream().findFirst().get().getScore() : 0;
    }
}
