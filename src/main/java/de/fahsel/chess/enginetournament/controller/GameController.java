package de.fahsel.chess.enginetournament.controller;

import de.fahsel.chess.enginetournament.configuration.EngineConfig;
import de.fahsel.chess.enginetournament.configuration.GameConfig;
import de.fahsel.chess.enginetournament.controller.dto.ConfigurationDTO;
import de.fahsel.chess.enginetournament.controller.dto.EngineDTO;
import de.fahsel.chess.enginetournament.model.event.NewGameEvent;
import de.fahsel.chess.enginetournament.model.game.Configuration;
import de.fahsel.chess.enginetournament.service.game.event.GameEventStream;
import de.fahsel.chess.enginetournament.service.gui.GuiEventStream;
import de.fahsel.chess.enginetournament.service.gui.event.GuiRefreshEvent;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GameController {
    private final GameEventStream gameEventStream;
    private final GuiEventStream guiEventStream;
    private final GameConfig gameConfig;

    public GameController(GameEventStream gameEventStream, GuiEventStream guiEventStream, GameConfig gameConfig) {
        this.gameEventStream = gameEventStream;
        this.guiEventStream = guiEventStream;
        this.gameConfig = gameConfig;
    }

    @GetMapping(value = "/gui-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> gui() {
        UnicastProcessor<ServerSentEvent<String>> unicastProcessor = UnicastProcessor.create();

        guiEventStream.subscribe(event -> unicastProcessor.sink().next(event));

        GuiRefreshEvent guiRefreshEvent = GuiRefreshEvent.builder()
                .newGame(false)
                .build();

        gameEventStream.publish(guiRefreshEvent);

        return unicastProcessor;
    }

    @GetMapping("/engines")
    public List<EngineDTO> getEngines() {
        return gameConfig.getEngines().entrySet().stream()
                .map(e -> EngineDTO.builder()
                        .id(e.getKey())
                        .name(e.getValue().getName())
                        .defaultWhite(e.getValue().isDefaultWhite())
                        .defaultBlack(e.getValue().isDefaultBlack())
                        .build())
                .collect(Collectors.toList());
    }

    @PostMapping("/game")
    public void startGame(@RequestBody ConfigurationDTO configurationDTO) {
        EngineConfig white = gameConfig.getEngines().get(configurationDTO.getEngineWhite());
        EngineConfig black = gameConfig.getEngines().get(configurationDTO.getEngineBlack());

        Configuration configuration = Configuration.builder()
                .whiteEngineConfig(white)
                .whiteEngineId(configurationDTO.getEngineWhite())
                .whiteEninePath(white.getPath())
                .whiteEngineName(white.getName())
                .blackEngineId(configurationDTO.getEngineBlack())
                .blackEngineConfig(black)
                .blackEnginePath(black.getPath())
                .blackEngineName(black.getName())
                .fen(configurationDTO.getFen())
                .clockSeconds(configurationDTO.getClockSeconds())
                .clockIncrementMillis(configurationDTO.getClockIncrementMillis())
                .multiPv(Integer.parseInt(configurationDTO.getMultiPv()))
                .badMoveThreshold(configurationDTO.getBadMoveThreshold())
                .build();

        gameEventStream.publish(
                NewGameEvent.builder()
                        .configuration(configuration)
                        .build()
        );
    }
}
