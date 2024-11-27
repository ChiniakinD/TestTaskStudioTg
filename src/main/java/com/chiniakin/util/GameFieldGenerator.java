package com.chiniakin.util;

import com.chiniakin.entity.Game;
import com.chiniakin.entity.GameField;
import com.chiniakin.enums.GameCell;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Класс отвечающий за генерацию игрового поля.
 *
 * @author ChiniakinD
 */
@Component
public class GameFieldGenerator {

    /**
     * Добавляет мины на поле случайным образом.
     *
     * @param height    высота поля.
     * @param width     ширина поля.
     * @param mines     количество мин.
     * @param gameField {@link GameField}.
     */
    public void addMines(int height, int width, int mines, GameField gameField) {
        Random random = new Random();
        GameCell[][] hiddenCells = gameField.getHiddenCells();
        while (mines > 0) {
            int h = random.nextInt(height);
            int w = random.nextInt(width);
            if (hiddenCells[h][w] == GameCell.EMPTY) {
                hiddenCells[h][w] = GameCell.MINE;
                mines -= 1;
            }
        }
        gameField.setHiddenCells(hiddenCells);
    }

    /**
     * Метод для перемещения мины на другое место, т.к. первый ход всегда должен быть безопасным.
     *
     * @param row       строка клетки на первом ходу по которой оказалась мина.
     * @param column    столбец клеткина первом ходу по которой оказалась мина.
     * @param game      {@link Game}.
     * @param gameField {@link GameField}.
     */
    public void moveMine(int row, int column, Game game, GameField gameField) {
        Random random = new Random();
        GameCell[][] hiddenCells = gameField.getHiddenCells();
        List<String> emptyCellCoordinates = new ArrayList<>();

        for (int r = 0; r < hiddenCells.length; r++) {
            for (int c = 0; c < hiddenCells[r].length; c++) {
                if (hiddenCells[r][c] != GameCell.MINE) {
                    emptyCellCoordinates.add("(" + r + ", " + c + ")");
                }
            }
        }
        hiddenCells[row][column] = GameCell.ZERO;

        int randomIndex = random.nextInt(emptyCellCoordinates.size());
        String randomCoordinate = emptyCellCoordinates.get(randomIndex);
        String[] coordinates = randomCoordinate.replace("(", "").replace(")", "").split(", ");
        int h = Integer.parseInt(coordinates[0]);
        int w = Integer.parseInt(coordinates[1]);

        hiddenCells[h][w] = GameCell.MINE;
        addMineCounts(game.getHeight(), game.getWidth(), gameField);
        gameField.setHiddenCells(hiddenCells);
    }

    /**
     * Добавляет  {@link GameCell} в каждую клетку согласно количества мин вокруг.
     *
     * @param height    высота поля.
     * @param width     ширина поля.
     * @param gameField {@link GameField}.
     */
    public void addMineCounts(int height, int width, GameField gameField) {
        GameCell[][] hiddenCells = gameField.getHiddenCells();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (hiddenCells[i][j] != GameCell.MINE) {
                    hiddenCells[i][j] = GameCell.getByValue(Integer.toString(countMinesAroundCell(i, j, height, width, hiddenCells)));
                }
            }
        }
        gameField.setHiddenCells(hiddenCells);
    }

    /**
     * Возвращает количество мин вокруг определенной клетки.
     *
     * @param row         строка клетки.
     * @param column      столбец клетки.
     * @param height      высота поля.
     * @param width       ширина поля.
     * @param hiddenCells состояние игрового поля.
     * @return количество мин вокруг клетки.
     */
    private int countMinesAroundCell(int row, int column, int height, int width, GameCell[][] hiddenCells) {
        int count = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {
                if (i < 0 || j < 0 || i >= height || j >= width) {
                    continue;
                }
                if (hiddenCells[i][j] == GameCell.MINE) {
                    count++;
                }
            }
        }
        return count;
    }

}
