package de.fahsel.chess.enginetournament.model.gui;

import de.fahsel.chess.enginetournament.controller.dto.ConfigurationDTO;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GuiRefresh {
    boolean newGame;
    List<String> moves;
    GuiPiece[][] pieces;
    List<GuiScore> scores;
    String engineWhite;
    String engineBlack;
    ConfigurationDTO configuration;
}
