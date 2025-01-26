package de.fahsel.chess.enginetournament.model.game;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode
public class Coordinate {
    private static final String COLUMNS = "abcdefgh";

    int row;
    int column;

    public static Coordinate of(String field) {
        if (field.length() != 2) {
            return null;
        }

        int column = COLUMNS.indexOf(String.valueOf(field.charAt(0)));
        int row = 8 - Integer.parseInt(String.valueOf(field.charAt(1)));

        return Coordinate.builder()
                .column(column)
                .row(row)
                .build();
    }

    public boolean is(String field) {
        Coordinate coordinate = Coordinate.of(field);

        return coordinate != null && coordinate.equals(this);
    }

    @Override
    public String toString() {
        return String.valueOf(COLUMNS.charAt(this.column)) + (8 - this.row);
    }
}
