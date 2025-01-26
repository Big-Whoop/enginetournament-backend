package de.fahsel.chess.enginetournament.service.game.event.handler;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.engine.Info;
import de.fahsel.chess.enginetournament.model.event.InitializeEngineEvent;
import de.fahsel.chess.enginetournament.model.system.ReactiveProcess;
import de.fahsel.chess.enginetournament.service.engine.DelegatingEngineEventHandler;
import de.fahsel.chess.enginetournament.service.system.ReactiveProcessRunner;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class InitializeEngineEventHandler implements GameEventHandler<InitializeEngineEvent> {
    private final ReactiveProcessRunner processRunner;
    private final DelegatingEngineEventHandler delegatingEngineEventHandler;

    public InitializeEngineEventHandler(ReactiveProcessRunner processRunner, DelegatingEngineEventHandler delegatingEngineEventHandler) {
        this.processRunner = processRunner;
        this.delegatingEngineEventHandler = delegatingEngineEventHandler;
    }

    @Override
    public void handle(InitializeEngineEvent gameEvent) {
        ReactiveProcess process = processRunner.run(
                gameEvent.getEnginePath(),
                "uci"
        );

        Info info = Info.builder()
                .pvs(new ConcurrentSkipListMap<>())
                .totalEventsReceived(new AtomicInteger(0))
                .build();

        Engine engine = Engine.builder()
                .side(gameEvent.getSide())
                .game(gameEvent.getGame())
                .options(gameEvent.getUciOptions())
                .commandSink(process.getInput())
                .info(info)
                .ready(false)
                .build();

        gameEvent.getGame().getEngines().put(engine.getSide(), engine);

        process.getOutput()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(lineFromEngine -> delegatingEngineEventHandler.handle(lineFromEngine, engine));
    }
}
