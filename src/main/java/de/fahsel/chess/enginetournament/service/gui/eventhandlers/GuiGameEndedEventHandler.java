package de.fahsel.chess.enginetournament.service.gui.eventhandlers;

import de.fahsel.chess.enginetournament.model.event.GameEndedEvent;
import de.fahsel.chess.enginetournament.model.game.GameState;
import de.fahsel.chess.enginetournament.model.gui.GuiGameEnded;
import de.fahsel.chess.enginetournament.service.game.event.handler.GameEventHandler;
import de.fahsel.chess.enginetournament.service.gui.GuiEventStream;
import org.springframework.stereotype.Service;

import java.util.Map;

import static de.fahsel.chess.enginetournament.model.game.GameState.*;
import static de.fahsel.chess.enginetournament.model.game.GameState.Reason.*;

@Service
public class GuiGameEndedEventHandler implements GameEventHandler<GameEndedEvent> {
    public static final Map<GameState, String> GAME_STATE_TRANSLATION = Map.of(
            DRAWN, "Remis",
            WHITE_WINS, "Weiss siegt",
            BLACK_WINS, "Schwarz siegt"
    );

    public static final Map<Reason, String> GAME_ENDED_REASONS_TRANSLATION = Map.of(
                    MATE, "Matt",
                    STALEMATE, "ersticktes Matt",
                    ENGINE_CRASH, "abgestürzte Engine",
                    FIFTY_MOVES, "50 Züge ohne Bauernzug oder geschlagene Figur",
                    NOT_ENOUGH_PIECES, "nicht genug Material",
                    REPEATED_POSITION, "dreifache Stellungswiederholung",
                    USERDEFINED_DRAWN_RULES, "benutzerdefinierte Remis-Regel"
            );

    private final GuiEventStream guiEventStream;

    public GuiGameEndedEventHandler(GuiEventStream guiEventStream) {
        this.guiEventStream = guiEventStream;
    }

    @Override
    public void handle(GameEndedEvent gameEvent) {
        guiEventStream.publish(
                "gameEnded",
                GuiGameEnded.builder()
                        .state(GAME_STATE_TRANSLATION.get(gameEvent.getGame().getState()))
                        .reason(GAME_ENDED_REASONS_TRANSLATION.get(gameEvent.getGame().getStateReason()))
                        .build()
        );
    }
}
