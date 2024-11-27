package com.chiniakin.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * Сущность, представляющая данные игры.
 *
 * @author ChiniakinD
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "game")
public class Game {

    @Id
    private UUID id;

    private int width;

    private int height;

    @JsonProperty("mines_count")
    private int minesCount;

    private boolean completed;

    @OneToOne
    @JoinColumn(name = "id")
    private GameField field;

}
