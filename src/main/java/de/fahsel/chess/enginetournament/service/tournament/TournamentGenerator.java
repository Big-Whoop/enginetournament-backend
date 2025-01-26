package de.fahsel.chess.enginetournament.service.tournament;

import de.fahsel.chess.enginetournament.configuration.GameConfig;
import de.fahsel.chess.enginetournament.model.tournament.Match;
import de.fahsel.chess.enginetournament.model.tournament.Result;
import de.fahsel.chess.enginetournament.model.tournament.Round;
import de.fahsel.chess.enginetournament.model.tournament.Tournament;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class TournamentGenerator {
    private final GameConfig gameConfig;

    public TournamentGenerator(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public Tournament generate() {
        Round round = Round.builder().build();

        Tournament tournament = Tournament.builder()
            .rounds(Collections.singletonList(round))
            .build();

        for (String engineWhite: gameConfig.getEngines().keySet()) {
            tournament.getResults().put(
                engineWhite,
                Result.builder()
                    .engineName(gameConfig.getEngines().get(engineWhite).getName())
                    .build()
            );

            for(String engineBlack: gameConfig.getEngines().keySet()) {
                if (engineWhite.equals(engineBlack)) {
                    continue;
                }

                Match match = Match.builder()
                    .engineIdWhite(engineWhite)
                    .engineIdBlack(engineBlack)
                    .build();

                round.getMatches().add(match);
            }

            Collections.shuffle(round.getMatches());
        }

        return tournament;
    }
}
