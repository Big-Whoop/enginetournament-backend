package de.fahsel.chess.enginetournament.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class GameEventStreamExecutorConfiguration {
    @Bean("gameEventStreamScheduler")
    public ScheduledExecutorService gameEventStreamExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
