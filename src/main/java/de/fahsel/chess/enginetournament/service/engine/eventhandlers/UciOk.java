package de.fahsel.chess.enginetournament.service.engine.eventhandlers;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.game.Game;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

@Service
@Log4j2
public class UciOk implements EngineEventHandler {
    @Override
    public void handle(String lineFromEngine, Engine engine) {
        Game game = engine.getGame();

        FluxSink<String> cmd = engine.getCommandSink();

        engine.setOption("MultiPV", game.getConfiguration().getMultiPv());
        engine.setOption("Threads", 24);
        engine.setOption("Hash", 16384);
        engine.setOption("UCI_ShowWDL" , true);

        engine.setOption("SmartPruningFactor", 1.0D);

        engine.getOptions().forEach(engine::setOption);

        cmd.next("ucinewgame");
        cmd.next("isready");
    }

    @Override
    public String command() {
        return "uciok";
    }
}
