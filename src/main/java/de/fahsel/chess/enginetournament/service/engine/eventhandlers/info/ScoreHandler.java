package de.fahsel.chess.enginetournament.service.engine.eventhandlers.info;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.engine.Info;
import de.fahsel.chess.enginetournament.service.engine.eventhandlers.util.EngineResponseTokens;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScoreHandler implements InfoHandler {
    @Override
    public void handle(EngineResponseTokens info, Engine engine) {
        Info engineInfo = engine.getInfo();
        Optional<String> afterScore = info.getFirstAfter("score");

        afterScore.ifPresent(cpOrMate -> {
           if (cpOrMate.equals("mate")) {
               info.getFirstIntAfter("mate").ifPresent(score -> {
                   if ((score > 0)) {
                       engineInfo.setScore(10000);
                   } else {
                       engineInfo.setScore(-10000);
                   }
               });
           } else {
               engineInfo.setScore(info.getFirstIntAfter("cp").orElse(0));
           }
        });
    }

    @Override
    public boolean handles(EngineResponseTokens info) {
        if (info.has("score") && info.has("multipv")) {
            return info.getFirstAfter("multipv").map(multipv -> multipv.equals("1")).orElse(false);
        }  else {
            return false;
        }
    }
}
