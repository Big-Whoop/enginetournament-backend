package de.fahsel.chess.enginetournament.service.engine.eventhandlers;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.engine.EngineOption;
import de.fahsel.chess.enginetournament.service.engine.eventhandlers.util.EngineResponseTokenizer;
import de.fahsel.chess.enginetournament.service.engine.eventhandlers.util.EngineResponseTokens;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.fahsel.chess.enginetournament.model.engine.EngineOption.Type.*;

@Service
public class Option implements EngineEventHandler{
    private final EngineResponseTokenizer engineResponseTokenizer;

    public Option(EngineResponseTokenizer engineResponseTokenizer) {
        this.engineResponseTokenizer = engineResponseTokenizer;
    }

    @Override
    public void handle(String lineFromEngine, Engine engine) {
        EngineResponseTokens tokens = engineResponseTokenizer.get(lineFromEngine);

        tokens.getFirstAfter("name").ifPresent(optionName -> {
            String typeString = tokens.getFirstAfter("type").orElse("string");
            EngineOption.Type type = EngineOption.Type.getOrDefault(typeString, STRING);
            String defaultValue = tokens.getFirstAfter("default").orElse("");

            List<String> comboOptions = tokens.getMultiValue("var");

            Double minValue = tokens.getFirstDoubleAfter("min").orElse(null);
            Double maxValue = tokens.getFirstDoubleAfter("max").orElse(null);

            engine.getAvailableOptions().put(
                optionName,
                EngineOption.builder()
                    .type(type)
                    .name(optionName)
                    .defaultValue(defaultValue)
                    .value(defaultValue)
                    .minValue(minValue)
                    .maxValue(maxValue)
                    .comboOptions(comboOptions)
                    .build());
        });
    }

    @Override
    public String command() {
        return "option";
    }
}
