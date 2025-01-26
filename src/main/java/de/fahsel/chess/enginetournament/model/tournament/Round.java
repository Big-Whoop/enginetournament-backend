package de.fahsel.chess.enginetournament.model.tournament;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Round {
    @Builder.Default
    private List<Match> matches = new ArrayList<>();

    @Builder.Default
    private int currentMatch = 1;
}
