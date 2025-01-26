package de.fahsel.chess.enginetournament.model.engine;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Optional;

@Value
@Builder
public class EngineOption {
    public enum Type {
        CHECK, STRING, SPIN, COMBO, BUTTON;

        public static Type getOrDefault(String stringType, Type defaultType) {
            try {
                return Type.valueOf(stringType.toUpperCase());
            } catch (IllegalArgumentException e) {
                return defaultType;
            }
        }
    }

    Type type;
    String name;
    String defaultValue;
    String value;
    Double minValue;
    Double maxValue;
    List<String> comboOptions;

    public Optional<Double> getMinValue() {
        return Optional.ofNullable(minValue);
    }

    public Optional<Double> getMaxValue() {
        return Optional.ofNullable(maxValue);
    }
}
