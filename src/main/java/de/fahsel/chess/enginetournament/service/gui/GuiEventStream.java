package de.fahsel.chess.enginetournament.service.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.DirectProcessor;
import reactor.core.scheduler.Schedulers;

import java.util.function.Consumer;

@Service
public class GuiEventStream {
    private final DirectProcessor<ServerSentEvent<String>> guiEventProcessor = DirectProcessor.create();
    private final ObjectMapper objectMapper;

    public GuiEventStream(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public void publish(String event, Object object) {
        String json = objectMapper.writeValueAsString(object);

        ServerSentEvent<String> serverSentEvent = ServerSentEvent.<String>builder()
                .event(event)
                .data(json)
                .build();

        guiEventProcessor.sink().next(serverSentEvent);
    }

    public void subscribe(Consumer<ServerSentEvent<String>> consumer) {
        guiEventProcessor
                .subscribeOn(Schedulers.single())
                .subscribe(consumer);
    }
}
