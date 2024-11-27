package com.chiniakin.entity;

import com.chiniakin.enums.GameCell;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сущность, представляющая игровое поле
 *
 * @author ChiniakinD
 */
@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "game_field")
public class GameField {

    /**
     * id игры, к которой относится игровое поле.
     */
    @Id
    private UUID id;

    /**
     * Двумерный массив видимых клеток игрового поля.
     */
    @Transient
    private GameCell[][] playerCells;

    /**
     * Сериализованное представление массива видимых клеток для сохранения в базу данных.
     */
    @Column(name = "player_cells")
    private String serializedPlayerCells;

    /**
     * Двумерный массив скрытых клеток игрового поля.
     */
    @Transient
    private GameCell[][] hiddenCells;

    /**
     * Сериализованное представление массива скрытых клеток для сохранения в базу данных.
     */
    @Column(name = "hidden_cells")
    private String serializedHiddenCells;

    /**
     * Сериализует массивы клеток игрового поля в строки для сохранения в базу данных.
     */
    public void serializeCells() {
        this.serializedPlayerCells = convertCellsToString(playerCells);
        this.serializedHiddenCells = convertCellsToString(hiddenCells);
    }

    /**
     * Десериализует строки из в базе данных, обратно в массивы клеток.
     */
    public void deserializeCells() {
        this.playerCells = convertStringToCells(serializedPlayerCells);
        this.hiddenCells = convertStringToCells(serializedHiddenCells);
    }

    /**
     * Преобразует двумерный массив клеток в строку, разделённую символами `,` и `;`.
     */
    private String convertCellsToString(GameCell[][] cells) {
        if (cells == null) {
            return null;
        }
        return Arrays.stream(cells)
                .map(row -> Arrays.stream(row)
                        .map(GameCell::getValue)
                        .collect(Collectors.joining(",")))
                .collect(Collectors.joining(";"));
    }

    /**
     * Преобразует строку из базы данных обратно в двумерный массив клеток.
     */
    private GameCell[][] convertStringToCells(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new GameCell[0][];
        }
        String[] rows = dbData.split(";");
        GameCell[][] cells = new GameCell[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] values = rows[i].split(",");
            cells[i] = new GameCell[values.length];
            for (int j = 0; j < values.length; j++) {
                cells[i][j] = GameCell.getByValue(values[j]);
            }
        }
        return cells;
    }

}
