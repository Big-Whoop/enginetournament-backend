package de.fahsel.chess.enginetournament.model.game;

public enum GameState {
    RUNNING, WHITE_WINS, BLACK_WINS, DRAWN;

    public enum Reason {
        MATE,
        ENGINE_CRASH,
        STALEMATE,
        FIFTY_MOVES,
        REPEATED_POSITION,
        USERDEFINED_DRAWN_RULES,
        NOT_ENOUGH_PIECES
    }
}
