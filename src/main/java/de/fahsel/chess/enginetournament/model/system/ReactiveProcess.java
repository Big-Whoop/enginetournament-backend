package de.fahsel.chess.enginetournament.model.system;

import lombok.Builder;
import lombok.Value;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Value
@Builder
public class ReactiveProcess {
    Flux<String> output;
    FluxSink<String> input;
}
