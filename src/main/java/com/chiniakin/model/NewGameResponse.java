package com.chiniakin.model;

import com.chiniakin.enums.GameCell;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * Объект ответа игры.
 *
 * @author ChiniakinD
 */
@Getter
@Setter
@Accessors(chain = true)
public class NewGameResponse {

    @JsonProperty("game_id")
    private UUID gameId;

    private int width;

    private int height;

    private int minesCount;

    private boolean completed;

    private GameCell[][] field;

}
