package de.fahsel.chess.enginetournament.model.tournament;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Result {
    private String engineId;
    private String engineName;

    @Builder.Default
    private int wins = 0;

    @Builder.Default
    private int losses = 0;

    @Builder.Default
    private int draws = 0;

    @Builder.Default
    private double score = 0;
}
