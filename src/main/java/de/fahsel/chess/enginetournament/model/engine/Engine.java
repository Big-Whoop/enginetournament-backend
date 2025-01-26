package de.fahsel.chess.enginetournament.model.engine;

import de.fahsel.chess.enginetournament.model.game.Game;
import de.fahsel.chess.enginetournament.model.game.Side;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.FluxSink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static de.fahsel.chess.enginetournament.model.engine.EngineOption.Type.*;

@Getter
@Builder
public class Engine {
    Game game;
    Side side;
    Info info;
    @Setter
    Boolean ready;
    FluxSink<String> commandSink;
    Map<String, String> options;
    @Builder.Default
    Map<String, EngineOption> availableOptions = new HashMap<>();

    public void setOption(String name, int value) {
        setOption(name, String.valueOf(value));
    }

    public void setOption(String name, double value) {
        setOption(name, String.valueOf(value));
    }

    public void setOption(String name, boolean value) {
        setOption(name, String.valueOf(value));
    }

    public void setOption(String name, String value) {
        Optional.ofNullable(availableOptions.get(name)).ifPresent(engineOption -> {
            if (engineOption.getType() == BUTTON) {
                commandSink.next("setoption name " + name);
            } else if (
                checkComboIfApplicable(engineOption, value) &&
                checkMinMaxIfApplicable(engineOption, value) &&
                checkCheckboxIfApplicable(engineOption, value)
            ) {
                commandSink.next("setoption name " + name + " value " + value);
            }
        });
    }

    private boolean checkCheckboxIfApplicable(EngineOption engineOption, String value) {
        return engineOption.getType() != CHECK || List.of("true", "false").contains(value);
    }

    private boolean checkMinMaxIfApplicable(EngineOption engineOption, String value) {
        if (engineOption.getType() != SPIN) {
            return true;
        }

        double doubleValue = Double.parseDouble(value);

        return (
            engineOption.getMinValue().stream()
            .map(minValue -> doubleValue >= minValue)
            .findAny().orElse(true)
        ) || (
            engineOption.getMaxValue().stream()
                .map(maxValue -> doubleValue <= maxValue)
                .findAny().orElse(true)
        );
    }

    private boolean checkComboIfApplicable(EngineOption engineOption, String value) {
        return engineOption.getType() != COMBO || engineOption.getComboOptions().contains(value);
    }
}
