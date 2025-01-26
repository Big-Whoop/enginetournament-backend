package de.fahsel.chess.enginetournament.service.tournament;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.tournament.Match;
import de.fahsel.chess.enginetournament.model.tournament.Result;
import de.fahsel.chess.enginetournament.model.tournament.Round;
import de.fahsel.chess.enginetournament.model.tournament.Tournament;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MatchResultRecorder {
    private final TournamentRegistry tournamentRegistry;

    public MatchResultRecorder(TournamentRegistry tournamentRegistry) {
        this.tournamentRegistry = tournamentRegistry;
    }

    public void record(Game game) {
        Tournament tournament = tournamentRegistry.getTournament();
        Map<String, Result> results = tournament.getResults();
        int currentRound = tournament.getCurrentRound();
        Round round = tournament.getRounds().get(currentRound - 1);
        int currentMatch = round.getCurrentMatch();
        Match match = round.getMatches().get(currentMatch - 1);

        match.setResult(game.getState());
        match.setReason(game.getStateReason());
    }
}
