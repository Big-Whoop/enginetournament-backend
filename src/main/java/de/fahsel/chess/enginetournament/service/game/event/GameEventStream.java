package de.fahsel.chess.enginetournament.service.game.event;

import de.fahsel.chess.enginetournament.model.event.GameEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.DirectProcessor;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Service
@Log4j2
public class GameEventStream {
    private final DirectProcessor<GameEvent> gameEventProcessor = DirectProcessor.create();
    private final ScheduledExecutorService gameEventStreamScheduler;

    public GameEventStream(@Qualifier("gameEventStreamScheduler") ScheduledExecutorService gameEventStreamExecutor) {
        this.gameEventStreamScheduler = gameEventStreamExecutor;
    }

    public void publish(GameEvent event) {
        publish(event, Duration.ZERO);
    }

    public void publish(GameEvent event, Duration delay) {
        gameEventStreamScheduler.schedule(() -> {
            gameEventProcessor.sink().next(event);
        }, delay.toMillis(), TimeUnit.MILLISECONDS);
    }

    public void cancelEverything() {

    }

    public void subscribe(Consumer<GameEvent> consumer, Class<? extends GameEvent> subcribeTo) {
        gameEventProcessor
                .subscribeOn(Schedulers.single())
                .filter(event -> event.getClass().equals(subcribeTo))
                .subscribe(consumer);
    }
}
