package com.chiniakin.controller;

import com.chiniakin.model.GameTurnRequest;
import com.chiniakin.model.NewGameRequest;
import com.chiniakin.service.interfaces.MinesweeperService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с запросами по игре "Сапер".
 *
 * @author ChiniakinD
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MinesweeperController {

    private final MinesweeperService minesweeperService;

    /**
     * Запрос на создание новой игры.
     *
     * @param newGameRequest - тело запроса на создание новой игры.
     * @return строку в формате json c информацией для новой игры.
     */
    @PostMapping("/new")
    public String newGame(@RequestBody NewGameRequest newGameRequest) {
        return minesweeperService.newGame(newGameRequest);
    }

    /**
     * Отправляет запрос на открытие клетки.
     *
     * @param gameTurnRequest тело запроса на открытие клетки в игре.
     * @return строку в формате json c информацией о результате выбоа.
     */
    @PostMapping("/turn")
    public String turnCell(@RequestBody GameTurnRequest gameTurnRequest) {
        return minesweeperService.turnCell(gameTurnRequest);
    }

}
