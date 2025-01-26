package de.fahsel.chess.enginetournament.model.game;

import de.fahsel.chess.enginetournament.configuration.EngineConfig;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Configuration {
    EngineConfig whiteEngineConfig;
    EngineConfig blackEngineConfig;
    String whiteEngineId;
    String whiteEngineName;
    String whiteEninePath;
    String blackEngineId;
    String blackEnginePath;
    String blackEngineName;
    String fen;
    int multiPv;
    int badMoveThreshold;
    int clockMinutes;
    int clockSeconds;
    int clockIncrementMillis;
}
