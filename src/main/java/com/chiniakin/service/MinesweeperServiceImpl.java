package com.chiniakin.service;

import com.chiniakin.entity.Game;
import com.chiniakin.entity.GameField;
import com.chiniakin.exception.BadRequestException;
import com.chiniakin.model.GameTurnRequest;
import com.chiniakin.model.NewGameRequest;
import com.chiniakin.model.NewGameResponse;
import com.chiniakin.repository.GameRepository;
import com.chiniakin.service.interfaces.MinesweeperService;
import com.chiniakin.util.CheckValidData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Реализация сервиса для управления игровым процессом.
 *
 * @author ChiniakinD
 */
@Service
@RequiredArgsConstructor
public class MinesweeperServiceImpl implements MinesweeperService {

    private final CheckValidData checkValidData;
    private final GameFieldServiceImpl gameFieldServiceImpl;
    private final ModelMapper modelMapper;
    private final GameRepository gameRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public String newGame(NewGameRequest newGameRequest) {
        checkValidData.checkNewGameRequest(newGameRequest);
        UUID gameId = UUID.randomUUID();
        Game game = new Game()
                .setId(gameId)
                .setHeight(newGameRequest.getHeight())
                .setWidth(newGameRequest.getWidth())
                .setMinesCount(newGameRequest.getMinesCount())
                .setCompleted(false);
        gameRepository.save(game);

        GameField gameField = gameFieldServiceImpl.createNewGameField(gameId, newGameRequest.getHeight(),
                newGameRequest.getWidth(), newGameRequest.getMinesCount());
        game.setField(gameField);

        return convertGameToString(modelMapper.map(game, NewGameResponse.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String turnCell(GameTurnRequest gameTurnRequest) {
        Game game = gameRepository.getGameByIdOrThrow(gameTurnRequest.getGameId());
        if (game.isCompleted()) {
            throw new BadRequestException("Игра завершена");
        }
        gameFieldServiceImpl.turnCell(gameTurnRequest.getRow(), gameTurnRequest.getColumn(), game);
        gameRepository.save(game);
        return convertGameToString(modelMapper.map(game, NewGameResponse.class));
    }

    /**
     * Конвертирует {@link NewGameResponse} в строковое представление в формате JSON.
     *
     * @return строковое представление объекта игры в формате JSON
     */
    private static String convertGameToString(NewGameResponse newGameResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(newGameResponse);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сериализации", e);
        }
    }

}
