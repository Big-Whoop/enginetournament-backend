package de.fahsel.chess.enginetournament.service.tournament;

import de.fahsel.chess.enginetournament.model.tournament.Tournament;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class TournamentRegistry {
    @Getter
    private Tournament tournament;

    private final TournamentGenerator tournamentGenerator;

    public TournamentRegistry(TournamentGenerator tournamentGenerator) {
        this.tournamentGenerator = tournamentGenerator;
    }

    public void create() {
        this.tournament = tournamentGenerator.generate();
    }
}
