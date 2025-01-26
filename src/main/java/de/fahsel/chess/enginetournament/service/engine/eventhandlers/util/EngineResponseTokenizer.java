package de.fahsel.chess.enginetournament.service.engine.eventhandlers.util;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Service
public class EngineResponseTokenizer {
    public EngineResponseTokens get(String lineFromEngine) {
        List<String> tokens = Collections.list(new StringTokenizer(lineFromEngine, " ")).stream()
                .map(token -> (String) token)
                .collect(Collectors.toList());

        return EngineResponseTokens.builder()
                .command(tokens.get(0))
                .tokens(tokens)
                .build();
    }
}
