package com.chiniakin.service;

import com.chiniakin.entity.Game;
import com.chiniakin.entity.GameField;
import com.chiniakin.enums.GameCell;
import com.chiniakin.exception.BadRequestException;
import com.chiniakin.repository.GameFieldRepository;
import com.chiniakin.service.interfaces.GameFieldService;
import com.chiniakin.util.GameFieldGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * Реализация сервиса для работы с игровым полем.
 *
 * @author ChiniakinD
 */
@Service
@RequiredArgsConstructor
public class GameFieldServiceImpl implements GameFieldService {

    private final GameFieldRepository gameFieldRepository;
    private final GameFieldGenerator gameFieldGenerator;
    private boolean isFirstTurn;

    /**
     * {@inheritDoc}
     */
    @Override
    public GameField createNewGameField(UUID id, int height, int width, int mines) {
        GameField gameField = new GameField()
                .setId(id);
        GameCell[][] playerCells = new GameCell[height][width];
        GameCell[][] hiddenCells = new GameCell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                playerCells[i][j] = GameCell.EMPTY;
                hiddenCells[i][j] = GameCell.EMPTY;
            }
        }
        gameField.setPlayerCells(playerCells);
        gameField.setHiddenCells(hiddenCells);
        gameFieldGenerator.addMines(height, width, mines, gameField);
        gameFieldGenerator.addMineCounts(height, width, gameField);

        saveGameField(gameField);
        isFirstTurn = true;
        return gameField;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void turnCell(int row, int column, Game game) {
        GameField gameField = gameFieldRepository.getGameFieldByIdOrThrow(game.getId());
        gameField.deserializeCells();
        if (!gameField.getPlayerCells()[row][column].equals(GameCell.EMPTY)) {
            throw new BadRequestException("Уже открытая ячейка");
        }
        if (gameField.getHiddenCells()[row][column].equals(GameCell.MINE)) {
            if (isFirstTurn) {
                gameFieldGenerator.moveMine(row, column, game, gameField);
                saveGameField(gameField);
                turnCell(row, column, game);
                isFirstTurn = false;
                return;
            }
            showGameOver(gameField);
            game.setCompleted(true);
            saveGameField(gameField);
            isFirstTurn = false;
            return;
        }
        openCell(row, column, game, gameField);
        isFirstTurn = false;
    }

    /**
     * Открывает клетки, если на нем нет мин и проверяет на победу.
     *
     * @param row       строка клетки.
     * @param column    столбец клетки.
     * @param game      {@link Game}.
     * @param gameField {@link GameField}.
     */
    private void openCell(int row, int column, Game game, GameField gameField) {
        if (!gameField.getHiddenCells()[row][column].equals(GameCell.ZERO)) {
            GameCell[][] playerCells = gameField.getPlayerCells();
            playerCells[row][column] = gameField.getHiddenCells()[row][column];
            gameField.setPlayerCells(playerCells);
            if (checkVictory(game, gameField)) {
                showVictory(gameField);
                game.setCompleted(true);
            }
            saveGameField(gameField);
            return;
        }
        openZeroCells(row, column, game, gameField);
        saveGameField(gameField);
    }

    /**
     * Открывает все пустые клетки, смежные с начальной клеткой.
     *
     * @param startRow строка начальной клетки.
     * @param startCol столбец начальной клетки.
     */
    private void openZeroCells(int startRow, int startCol, Game game, GameField gameField) {
        Set<String> zeroCells = new HashSet<>();
        zeroCells.add(startRow + "," + startCol);
        GameCell[][] playerCells = gameField.getPlayerCells();

        while (!zeroCells.isEmpty()) {
            Iterator<String> iterator = zeroCells.iterator();
            String key = iterator.next();
            iterator.remove();

            String[] parts = key.split(",");
            int row = Integer.parseInt(parts[0]);
            int column = Integer.parseInt(parts[1]);

            playerCells[row][column] = gameField.getHiddenCells()[row][column];
            gameField.setPlayerCells(playerCells);

            checkAndAddAdjacentCell(row - 1, column, zeroCells, game, gameField);
            checkAndAddAdjacentCell(row + 1, column, zeroCells, game, gameField);
            checkAndAddAdjacentCell(row, column - 1, zeroCells, game, gameField);
            checkAndAddAdjacentCell(row, column + 1, zeroCells, game, gameField);
            if (checkVictory(game, gameField)) {
                showVictory(gameField);
                game.setCompleted(true);
            }
        }
    }

    /**
     * Проверяет и добавляет соседей в коллекцию смежных клеток.
     *
     * @param row       строка клетки.
     * @param column    столбец клетки.
     * @param zeroCells коллекция строковых представлений пустых клеток, в которые нужно добавить координаты.
     */
    private void checkAndAddAdjacentCell(int row, int column, Set<String> zeroCells, Game game, GameField gameField) {
        if (row >= 0 && row < game.getHeight() && column >= 0 && column < game.getWidth()) {
            String coordinates = row + "," + column;
            if (gameField.getPlayerCells()[row][column] == GameCell.EMPTY) {
                if (gameField.getHiddenCells()[row][column] != GameCell.ZERO) {
                    gameField.getPlayerCells()[row][column] = gameField.getHiddenCells()[row][column];
                } else {
                    zeroCells.add(coordinates);
                }
            }
        }
    }

    /**
     * Показывает скрытые клетки и изменяя клетки с минами на клетки с взрывами. в случае проигрыша.
     */
    private void showGameOver(GameField gameField) {
        gameField.setPlayerCells(gameField.getHiddenCells());
        for (int i = 0; i < gameField.getPlayerCells().length; i++) {
            for (int j = 0; j < gameField.getPlayerCells()[0].length; j++) {
                if (gameField.getHiddenCells()[i][j].equals(GameCell.MINE)) {
                    gameField.getPlayerCells()[i][j] = GameCell.EXPLOSION;
                }
            }
        }
    }

    /**
     * Проверяет, достигнута ли победа в игре.
     *
     * @return true, если все клетки без мин открыты.
     */
    private boolean checkVictory(Game game, GameField gameField) {
        int mines = game.getMinesCount();
        int emptyCell = (int) Arrays.stream(gameField.getPlayerCells())
                .flatMap(Arrays::stream)
                .filter(cell -> cell == GameCell.EMPTY)
                .count();

        return mines == emptyCell;
    }

    /**
     * Обрабатывает окончание игры с победой, показывая скрытые клетки и оставляя клетки с минами в их исходном виде.
     */
    private void showVictory(GameField gameField) {
        gameField.setPlayerCells(gameField.getHiddenCells());
        for (int i = 0; i < gameField.getPlayerCells().length; i++) {
            for (int j = 0; j < gameField.getPlayerCells()[0].length; j++) {
                if (gameField.getHiddenCells()[i][j].equals(GameCell.MINE)) {
                    gameField.getPlayerCells()[i][j] = GameCell.MINE;
                }
            }
        }
    }

    /**
     * Сериализует клетки игрового поля, далее сохраняет игровое поле в базе данных.
     */
    private void saveGameField(GameField gameField) {
        gameField.serializeCells();
        gameFieldRepository.save(gameField);
    }

}

