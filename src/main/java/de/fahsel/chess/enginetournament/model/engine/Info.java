package de.fahsel.chess.enginetournament.model.engine;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
public class Info {
    private Integer score;
    private WinDrawLoss winDrawLoss;
    private Depth depth;
    private ConcurrentSkipListMap<Integer, Pv> pvs;
    private AtomicInteger totalEventsReceived;

    public WinDrawLoss getWinDrawLoss() {
        return Optional.ofNullable(winDrawLoss)
            .orElse(WinDrawLoss.builder()
                .win(-1).draw(-1).loss(-1)
                .build());
    }

    private WinDrawLoss fromScore() {
        return WinDrawLoss.builder().build();
    }
}
