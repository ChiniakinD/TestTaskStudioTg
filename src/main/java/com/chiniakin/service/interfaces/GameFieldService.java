package com.chiniakin.service.interfaces;

import com.chiniakin.entity.Game;
import com.chiniakin.entity.GameField;

import java.util.UUID;

public interface GameFieldService {

    /**
     * Создает новое игровое поле.
     *
     * @param id     id поля.
     * @param height высота поля.
     * @param width  ширина поля.
     * @param mines  количество мин.
     * @return GameField.
     */
    GameField createNewGameField(UUID id, int height, int width, int mines);

    /**
     * Открывает клетку на поле.
     *
     * @param row    номер строки.
     * @param column номер столбца.
     */
    void turnCell(int row, int column, Game game);

}
