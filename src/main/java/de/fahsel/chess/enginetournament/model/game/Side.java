package de.fahsel.chess.enginetournament.model.game;

public enum Side {
    WHITE, BLACK;

    public Side opposite() {
        return this.equals(WHITE) ? BLACK : WHITE;
    }
}
