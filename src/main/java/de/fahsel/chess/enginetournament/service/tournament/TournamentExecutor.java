package de.fahsel.chess.enginetournament.service.tournament;

import de.fahsel.chess.enginetournament.configuration.EngineConfig;
import de.fahsel.chess.enginetournament.configuration.GameConfig;
import de.fahsel.chess.enginetournament.model.game.Configuration;
import de.fahsel.chess.enginetournament.model.tournament.Match;
import de.fahsel.chess.enginetournament.model.tournament.Round;
import de.fahsel.chess.enginetournament.model.tournament.Tournament;
import org.springframework.stereotype.Service;

@Service
public class TournamentExecutor {
    private final TournamentRegistry tournamentRegistry;
    private final GameConfig gameConfig;

    public TournamentExecutor(TournamentRegistry tournamentRegistry, GameConfig gameConfig) {
        this.tournamentRegistry = tournamentRegistry;
        this.gameConfig = gameConfig;
    }

    public Configuration next() {
        Tournament tournament = tournamentRegistry.getTournament();

        int currentRound = tournament.getCurrentRound();

        Round round = tournament.getRounds().get(currentRound - 1);

        int currentMatch = round.getCurrentMatch();

        if (currentMatch == round.getMatches().size()) {
            tournament.setCurrentRound(currentRound + 1);
        } else {
            round.setCurrentMatch(currentMatch + 1);
        }

        return start();
    }

    public Configuration start() {
        Tournament tournament = tournamentRegistry.getTournament();

        Round round = tournament.getRounds().get(tournament.getCurrentRound() - 1);
        Match match = round.getMatches().get(round.getCurrentMatch() - 1);

        EngineConfig white = gameConfig.getEngines().get(match.getEngineIdWhite());
        EngineConfig black = gameConfig.getEngines().get(match.getEngineIdBlack());

        return Configuration.builder()
            .whiteEninePath(white.getPath())
            .whiteEngineName(white.getName())
            .blackEnginePath(black.getPath())
            .blackEngineName(black.getName())
            .fen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
            .multiPv(7)
            .build();
    }
}
