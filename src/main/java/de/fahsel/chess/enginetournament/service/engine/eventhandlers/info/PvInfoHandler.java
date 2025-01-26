package de.fahsel.chess.enginetournament.service.engine.eventhandlers.info;

import de.fahsel.chess.enginetournament.model.engine.Engine;
import de.fahsel.chess.enginetournament.model.engine.Pv;
import de.fahsel.chess.enginetournament.model.event.PvReceivedEvent;
import de.fahsel.chess.enginetournament.model.game.Coordinate;
import de.fahsel.chess.enginetournament.service.engine.eventhandlers.util.EngineResponseTokens;
import de.fahsel.chess.enginetournament.service.game.event.GameEventStream;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentSkipListMap;

@Service
@Log4j2
public class PvInfoHandler implements InfoHandler {
    private final GameEventStream gameEventStream;

    public PvInfoHandler(GameEventStream gameEventStream) {
        this.gameEventStream = gameEventStream;
    }

    @Override
    public void handle(EngineResponseTokens info, Engine engine) {
        info.getFirstAfter("pv").ifPresent(move -> {
            Coordinate from = Coordinate.of(move.substring(0, 2));
            Coordinate to = Coordinate.of(move.substring(2, 4));

            String scoreType = info.getFirstAfter("score").orElse("none");

            int score = 0;
            if (scoreType.equals("cp")) {
                score = info.getFirstIntAfter("cp").orElse(0);
            } else if (scoreType.equals("mate")) {
                if (info.getFirstIntAfter("mate").orElse(0) > 0) {
                    score = 10000;
                } else {
                    score = -10000;
                }
            }

            String content = String.join(" ", info.getAllAfter("info"));

            int multiPv = info.getFirstIntAfter("multipv").orElse(1);

            Pv pv = Pv.builder()
                .side(engine.getSide())
                .index(multiPv)
                .score(score)
                .win(engine.getInfo().getWinDrawLoss().getWin() / 10)
                .draw(engine.getInfo().getWinDrawLoss().getDraw() / 10)
                .loss(engine.getInfo().getWinDrawLoss().getLoss() / 10)
                .from(from)
                .to(to)
                .content(content)
                .build();

            ConcurrentSkipListMap<Integer, Pv> pvs = engine.getInfo().getPvs();

            pvs.put(pv.getIndex(), pv);

            calculateWeights(pvs, engine.getGame().getConfiguration().getBadMoveThreshold());

            PvReceivedEvent pvReceivedEvent = PvReceivedEvent.builder()
                .pvs(pvs)
                .build();

            gameEventStream.publish(pvReceivedEvent);
        } );

    }

    @Override
    public boolean handles(EngineResponseTokens info) {
        return info.has("pv");
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private void calculateWeights(ConcurrentSkipListMap<Integer, Pv> pvs, int badMoveThreshold) {
        int minWeight = pvs.values().stream()
                .map(Pv::getScore)
                .min(Integer::compareTo)
                .get();

        int maxWeight = pvs.values().stream()
                .map(Pv::getScore)
                .max(Integer::compareTo)
                .get();

        for (Pv pv: pvs.values()) {
            int score = pv.getScore();

            if ((maxWeight - minWeight) == 0 || pv.getIndex() == 1) {
                pv.setWeight(1);
            } else {
                if ((maxWeight - score) < badMoveThreshold) {
                    pv.setWeight((badMoveThreshold - (double) (maxWeight - score)) / badMoveThreshold);
                } else {
                    pv.setWeight(0);
                }
            }
        }
    }
}
