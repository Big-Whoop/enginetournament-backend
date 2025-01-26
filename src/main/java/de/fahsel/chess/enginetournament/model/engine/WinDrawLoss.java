package de.fahsel.chess.enginetournament.model.engine;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class WinDrawLoss {
    int win;
    int draw;
    int loss;
}
