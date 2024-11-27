package com.chiniakin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс тела запроса на создание новой игры.
 *
 * @author ChiniakinD
 */
@Getter
@Setter
public class NewGameRequest {

    private int width;

    private int height;

    @JsonProperty("mines_count")
    private int minesCount;

}
