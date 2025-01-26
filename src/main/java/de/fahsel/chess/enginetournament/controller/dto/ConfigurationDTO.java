package de.fahsel.chess.enginetournament.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfigurationDTO{
	private String engineWhite;
	private String engineBlack;
	private int clockMinutes;
	private int clockSeconds;
	private int clockIncrementMillis;
	private String multiPv;
	private int badMoveThreshold;
	private String fen;
}
