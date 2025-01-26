package de.fahsel.chess.enginetournament.local;

import lombok.Builder;

import java.time.Duration;

public class ChessClock {
    private Duration timeRemaining;
    private final int millisIncrement;
    private long startedMillis;
    private boolean isRunning = false;

    @Builder
    private ChessClock(int minutes, int seconds, int millisIncrement) {
        this.timeRemaining = Duration.ofSeconds(minutes * 60L + seconds);
        this.millisIncrement = millisIncrement;
    }

    public void start() {
        if (isRunning) {
            return;
        }

        isRunning = true;
        startedMillis = System.currentTimeMillis();
    }

    public void stop() {
        if (!isRunning) {
            return;
        }

        isRunning = false;
        long passedMillis = System.currentTimeMillis() - startedMillis;

        timeRemaining = timeRemaining
                .minus(Duration.ofMillis(passedMillis))
                .plus(Duration.ofMillis(millisIncrement)
            );

        if (timeRemaining.minus(Duration.ofMillis(millisIncrement)).isNegative()) {
            timeRemaining = Duration.ofMillis(millisIncrement);
        }
    }


    public long millisRemaining() {
        return timeRemaining.toMillis();
    }

    public Duration timeRemaining() {
        if (isRunning) {
            long passedMillis = System.currentTimeMillis() - startedMillis;

            return timeRemaining.minusMillis(passedMillis);
        } else {
            return timeRemaining;
        }
    }
}
