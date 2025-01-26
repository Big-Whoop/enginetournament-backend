package de.fahsel.chess.enginetournament.service.game.event.handler;

import de.fahsel.chess.enginetournament.model.event.GameReadyEvent;
import de.fahsel.chess.enginetournament.model.event.InitializeEngineEvent;
import de.fahsel.chess.enginetournament.model.event.NewGameEvent;
import de.fahsel.chess.enginetournament.model.event.UnregisterGameEvent;
import de.fahsel.chess.enginetournament.model.game.Configuration;
import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Side;
import de.fahsel.chess.enginetournament.service.game.GameRegistry;
import de.fahsel.chess.enginetournament.service.game.engine.FenInitializer;
import de.fahsel.chess.enginetournament.service.game.event.GameEventStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class NewGameEventHandler implements GameEventHandler<NewGameEvent> {
    private final FenInitializer fenInitializer;
    private final GameEventStream gameEventStream;
    private final GameRegistry gameRegistry;

    public NewGameEventHandler(FenInitializer fenInitializer, GameEventStream gameEventStream, GameRegistry gameRegistry) {
        this.fenInitializer = fenInitializer;
        this.gameEventStream = gameEventStream;
        this.gameRegistry = gameRegistry;
    }

    @Override
    public void handle(NewGameEvent gameEvent) {
        unregisterOldGame();
        registerNewGame(gameEvent);
    }

    private void unregisterOldGame() {
        Game oldGame = gameRegistry.get();

        if (oldGame != null) {
            UnregisterGameEvent unregisterGameEvent = UnregisterGameEvent.builder()
                    .game(oldGame)
                    .build();

            gameEventStream.publish(unregisterGameEvent);
        }
    }

    private void registerNewGame(NewGameEvent gameEvent) {
        Configuration configuration = gameEvent.getConfiguration();
        Game game = gameRegistry.create(configuration);

        fenInitializer.initialize(game, configuration.getFen());

        GameReadyEvent gameReadyEvent = GameReadyEvent.builder()
                .game(game)
                .build();

        InitializeEngineEvent white = InitializeEngineEvent.builder()
                .enginePath(configuration.getWhiteEninePath())
                .uciOptions(configuration.getWhiteEngineConfig().getOptions())
                .side(Side.WHITE)
                .game(game)
                .build();

        InitializeEngineEvent black = InitializeEngineEvent.builder()
                .enginePath(configuration.getBlackEnginePath())
                .uciOptions(configuration.getBlackEngineConfig().getOptions())
                .side(Side.BLACK)
                .game(game)
                .build();

        gameEventStream.publish(gameReadyEvent);
        gameEventStream.publish(white);
        gameEventStream.publish(black);
    }
}
