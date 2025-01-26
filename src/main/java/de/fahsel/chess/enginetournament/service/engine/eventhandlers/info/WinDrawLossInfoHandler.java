package de.fahsel.chess.enginetournament.service.engine.eventhandlers.info;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.engine.Info;
import de.fahsel.chess.enginetournament.model.engine.WinDrawLoss;
import de.fahsel.chess.enginetournament.service.engine.eventhandlers.util.EngineResponseTokens;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WinDrawLossInfoHandler implements InfoHandler {
    @Override
    public void handle(EngineResponseTokens info, Engine engine) {
        Info engineInfo = engine.getInfo();

        List<String> tokens = info.getAllAfter("wdl").subList(0, 3);

        engineInfo.setWinDrawLoss(
            WinDrawLoss.builder()
                .win(Integer.parseInt(tokens.get(0)))
                .draw(Integer.parseInt(tokens.get(1)))
                .loss(Integer.parseInt(tokens.get(2)))
            .build()
        );
    }

    @Override
    public boolean handles(EngineResponseTokens info) {
        if (info.has("wdl") && info.has("multipv")) {
            return info.getFirstAfter("multipv").map(multipv -> multipv.equals("1")).orElse(false);
        }  else {
            return false;
        }
    }
}
