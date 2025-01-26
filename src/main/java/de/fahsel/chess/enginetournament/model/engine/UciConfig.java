package de.fahsel.chess.enginetournament.model.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UciConfig {
    private Map<String, String> config = new ConcurrentHashMap<>();
}
