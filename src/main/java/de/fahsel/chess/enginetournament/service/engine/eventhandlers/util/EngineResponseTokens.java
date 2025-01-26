package de.fahsel.chess.enginetournament.service.engine.eventhandlers.util;

import lombok.Builder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Builder
public class EngineResponseTokens {
    String command;
    List<String> tokens;

    public boolean has(String token) {
        return tokens.contains(token);
    }

    public Optional<Integer> getFirstIntAfter(String token) {
       return getFirstAfter(token).map(Integer::parseInt);
    }

    public Optional<Double> getFirstDoubleAfter(String token) {
        return getFirstAfter(token).map(Double::parseDouble);
    }

    public Optional<String> getFirstAfter(String token) {
        int tokenIndex = tokens.indexOf(token);

        if (tokenIndex > -1 && tokens.size() > (tokenIndex + 1)) {
            return Optional.of(tokens.get(tokenIndex + 1));
        }

        return Optional.empty();
    }

    public List<String> getAllAfter(String token) {
        int tokenIndex = tokens.indexOf(token);

        if (tokenIndex > -1 && tokens.size() > (tokenIndex + 1)) {
            return tokens.subList(tokenIndex + 1, tokens.size());
        }

        return Collections.emptyList();
    }

    public List<String> getMultiValue(String token) {
        List<String> multiValue = new LinkedList<>();

        for (int i = 0; i < tokens.size(); i++) {
            String iteratedToken = tokens.get(i);
            if (iteratedToken.equals(token) && i + 1 < tokens.size()) {
                multiValue.add(tokens.get(i + 1));
            }
        }

        return multiValue;
    }
}
