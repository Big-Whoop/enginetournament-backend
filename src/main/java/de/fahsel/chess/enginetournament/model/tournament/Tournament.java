package de.fahsel.chess.enginetournament.model.tournament;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class Tournament {
    @Builder.Default
    private List<Round> rounds = new ArrayList<>();

    @Builder.Default
    private int currentRound = 1;

    @Builder.Default
    private Map<String, Result> results = new HashMap<>();
}
