package de.fahsel.chess.enginetournament.model.engine;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Depth {
    private int depth;
    private int selDepth;
}
