package de.fahsel.chess.enginetournament.service.gui.eventhandlers;

import de.fahsel.chess.enginetournament.controller.dto.ConfigurationDTO;
import de.fahsel.chess.enginetournament.model.game.*;
import de.fahsel.chess.enginetournament.model.gui.GuiGameEnded;
import de.fahsel.chess.enginetournament.model.gui.GuiPiece;
import de.fahsel.chess.enginetournament.model.gui.GuiRefresh;
import de.fahsel.chess.enginetournament.model.gui.GuiScore;
import de.fahsel.chess.enginetournament.service.game.GameRegistry;
import de.fahsel.chess.enginetournament.service.game.event.handler.GameEventHandler;
import de.fahsel.chess.enginetournament.service.gui.GameToGuiPiecesConverter;
import de.fahsel.chess.enginetournament.service.gui.GuiEventStream;
import de.fahsel.chess.enginetournament.service.gui.event.GuiRefreshEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static de.fahsel.chess.enginetournament.service.gui.eventhandlers.GuiGameEndedEventHandler.GAME_ENDED_REASONS_TRANSLATION;
import static de.fahsel.chess.enginetournament.service.gui.eventhandlers.GuiGameEndedEventHandler.GAME_STATE_TRANSLATION;

@Service
public class GuiRefreshEventHandler implements GameEventHandler<GuiRefreshEvent> {
    private final GameRegistry gameRegistry;
    private final GuiEventStream guiEventStream;
    private final GameToGuiPiecesConverter gameToGuiPiecesConverter;

    public GuiRefreshEventHandler(GameRegistry gameRegistry, GuiEventStream guiEventStream, GameToGuiPiecesConverter gameToGuiPiecesConverter) {
        this.gameRegistry = gameRegistry;
        this.guiEventStream = guiEventStream;
        this.gameToGuiPiecesConverter = gameToGuiPiecesConverter;
    }

    @Override
    public void handle(GuiRefreshEvent gameEvent) {
        Game game = gameRegistry.get();

        if (game != null) {
            GuiPiece[][] pieces = gameToGuiPiecesConverter.convert(game.getPieces());

            List<GuiScore> guiScores = getGuiScores(game);

            List<String> moves = game.getMoves().stream()
                .map(Move::getRawMove)
                .collect(Collectors.toList());

            GuiRefresh guiRefresh = GuiRefresh.builder()
                .pieces(pieces)
                .scores(guiScores)
                .newGame(gameEvent.isNewGame())
                .engineWhite(game.getConfiguration().getWhiteEngineName())
                .engineBlack(game.getConfiguration().getBlackEngineName())
                .configuration(configDTOfromConfig(game.getConfiguration()))
                .moves(moves)
                .build();

            guiEventStream.publish("refresh", guiRefresh);

            if (!game.getState().equals(GameState.RUNNING)) {
                guiEventStream.publish(
                        "gameEnded",
                        GuiGameEnded.builder()
                                .state(GAME_STATE_TRANSLATION.get(game.getState()))
                                .reason(GAME_ENDED_REASONS_TRANSLATION.get(game.getStateReason()))
                                .build()
                );
            }
        }
    }

    private ConfigurationDTO configDTOfromConfig(Configuration configuration) {
        return ConfigurationDTO.builder()
            .engineWhite(configuration.getWhiteEngineId())
            .engineBlack(configuration.getBlackEngineId())
            .clockMinutes(configuration.getClockMinutes())
            .clockSeconds(configuration.getClockSeconds())
            .clockIncrementMillis(configuration.getClockIncrementMillis())
            .fen(configuration.getFen())
            .multiPv(String.valueOf(configuration.getMultiPv()))
            .badMoveThreshold(configuration.getBadMoveThreshold())
            .build();

    }

    private List<GuiScore> getGuiScores(Game game) {
        int blackScore = 0;
        int whiteScore = 0;
        int index = 1;

        List<GuiScore> guiScores = new ArrayList<>();

        for (Move move: game.getMoves()) {
            if (move.getPiece().getSide().equals(Side.WHITE)) {
                whiteScore = move.getScore();
            } else {
                blackScore = move.getScore();
            }

            GuiScore guiScore = GuiScore.builder()
                    .index(index)
                    .white(limit(whiteScore))
                    .black(limit(- blackScore))
                    .build();

            guiScores.add(guiScore);

            index++;
        }

        return guiScores;
    }

    private int limit(int value) {
        return Math.max(Math.min(value, 1000), -1000);
    }
}
