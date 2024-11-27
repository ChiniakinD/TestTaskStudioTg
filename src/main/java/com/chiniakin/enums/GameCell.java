package com.chiniakin.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Перечисление возможных значений клеток;
 *
 * @author ChiniakinD
 */
public enum GameCell {

    EMPTY(" "),
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    EXPLOSION("X"),
    MINE("M");

    private final String value;

    GameCell(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Получает enum по его значению.
     *
     * @param value значение.
     */
    public static GameCell getByValue(String value) {
        for (GameCell cell : values()) {
            if (cell.value.equals(value)) {
                return cell;
            }
        }
        throw new RuntimeException();
    }

}
