package de.fahsel.chess.enginetournament.controller.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EngineDTO {
    String id;
    String name;
    boolean defaultWhite;
    boolean defaultBlack;
}
