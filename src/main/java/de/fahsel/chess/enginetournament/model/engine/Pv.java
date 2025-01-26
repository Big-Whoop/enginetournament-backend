package de.fahsel.chess.enginetournament.model.engine;

import de.fahsel.chess.enginetournament.model.game.Coordinate;
import de.fahsel.chess.enginetournament.model.game.Side;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pv {
    private Side side;
    private String content;
    private int index;
    private int score;
    private int win;
    private int draw;
    private int loss;
    private double weight;
    private Coordinate from;
    private Coordinate to;
}
