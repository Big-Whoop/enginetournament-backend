package de.fahsel.chess.enginetournament.service;

import de.fahsel.chess.enginetournament.model.event.NewGameEvent;
import de.fahsel.chess.enginetournament.model.game.Configuration;
import de.fahsel.chess.enginetournament.service.game.event.GameEventStream;
import de.fahsel.chess.enginetournament.service.tournament.TournamentExecutor;
import de.fahsel.chess.enginetournament.service.tournament.TournamentRegistry;
import org.springframework.stereotype.Service;

@Service
public class Test {
    private final GameEventStream gameEventStream;
    private final TournamentExecutor tournamentExecutor;
    private final TournamentRegistry tournamentRegistry;

    public Test(GameEventStream gameEventStream, TournamentExecutor tournamentExecutor, TournamentRegistry tournamentRegistry) {
        this.gameEventStream = gameEventStream;
        this.tournamentExecutor = tournamentExecutor;
        this.tournamentRegistry = tournamentRegistry;
    }

    public void testTournament() throws InterruptedException {
        tournamentRegistry.create();

        Configuration config = tournamentExecutor.start();

        NewGameEvent newGameEvent = NewGameEvent.builder()
            .configuration(config)
            .build();

        Thread.sleep(2000);

        gameEventStream.publish(newGameEvent);
    }
}
