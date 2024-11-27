package com.chiniakin.service.interfaces;

import com.chiniakin.model.GameTurnRequest;
import com.chiniakin.model.NewGameRequest;

public interface MinesweeperService {

    /**
     * Создает новую игру.
     *
     * @param newGameRequest - тело запроса на создание новой игры.
     * @return строку в формате json c информацией для новой игры.
     */
    String newGame(NewGameRequest newGameRequest);

    /**
     * Открывает выбранную клетку.
     *
     * @param gameTurnRequest тело запроса на открытие клетки в игре.
     * @return строку в формате json c информацией о результате выбора.
     */
    String turnCell(GameTurnRequest gameTurnRequest);

}
