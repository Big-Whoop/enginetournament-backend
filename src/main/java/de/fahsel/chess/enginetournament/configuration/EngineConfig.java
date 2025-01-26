package de.fahsel.chess.enginetournament.configuration;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EngineConfig {
    private String name;
    private String path;
    private boolean defaultWhite = false;
    private boolean defaultBlack = false;
    private Map<String, String> options = new HashMap<>();
}
