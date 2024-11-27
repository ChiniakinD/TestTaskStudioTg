package com.chiniakin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Класс тела запроса на открытие клетки в игре.
 *
 * @author ChiniakinD
 */
@Getter
@Setter
public class GameTurnRequest {

    @JsonProperty("game_id")
    private UUID gameId;

    @JsonProperty("col")
    private int column;

    private int row;

}
