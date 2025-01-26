package de.fahsel.chess.enginetournament.service.gui;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Side;
import de.fahsel.chess.enginetournament.model.gui.GuiClocks;
import de.fahsel.chess.enginetournament.service.game.GameRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static de.fahsel.chess.enginetournament.model.game.GameState.RUNNING;

@Service
public class ClockEmitter {
    private final GameRegistry gameRegistry;
    private final GuiEventStream guiEventStream;

    public ClockEmitter(GameRegistry gameRegistry, GuiEventStream guiEventStream) {
        this.gameRegistry = gameRegistry;
        this.guiEventStream = guiEventStream;
    }

    @Scheduled(fixedDelay = 50)
    public void updateClocks() {
        Game game = gameRegistry.get();

        if (game != null && game.getState().equals(RUNNING)) {
            guiEventStream.publish("clocks",
                GuiClocks.builder()
                    .white(formatClock(game, Side.WHITE))
                    .black(formatClock(game, Side.BLACK))
                    .build()
                );
        }
    }

    private String formatClock(Game game, Side side) {
        Duration remainingTime = game.getClock().get(side).timeRemaining();
        String engineName = side.equals(Side.WHITE) ?
            game.getConfiguration().getWhiteEngineName() :
            game.getConfiguration().getBlackEngineName();
        long hours = remainingTime.toHours();
        long minutes = remainingTime.toMinutesPart();
        long seconds = remainingTime.toSecondsPart();
        long deciSeconds = remainingTime.toMillisPart() / 100;

        return String.format("%s (%d:%02d:%02d.%d)", engineName, hours, minutes, seconds, deciSeconds);

    }
}
