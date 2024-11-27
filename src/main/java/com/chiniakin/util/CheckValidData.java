package com.chiniakin.util;

import com.chiniakin.exception.BadRequestException;
import com.chiniakin.model.NewGameRequest;
import org.springframework.stereotype.Component;

/**
 * Сервис для проверки корректности поступающих данных для создания новой игыр.
 *
 * @author ChiniakinD
 */
@Component
public class CheckValidData {

    public static final int MIN_SIZE_VALUE = 2;
    private final int MAX_SIZE_VALUE = 30;
    private final int MIN_MINE_COUNT = 1;

    public void checkNewGameRequest(NewGameRequest newGameRequest) throws BadRequestException {

        if (newGameRequest.getWidth() > MAX_SIZE_VALUE || newGameRequest.getWidth() < MIN_SIZE_VALUE) {
            throw new BadRequestException("Ширина поля должна быть не менее 2 и не более 30");
        }

        if (newGameRequest.getHeight() > MAX_SIZE_VALUE || newGameRequest.getHeight() < MIN_SIZE_VALUE) {
            throw new BadRequestException("Высота поля должна быть не менее 2 и не более 30");
        }

        int maxMineCount = countMaxMine(newGameRequest);
        if (newGameRequest.getMinesCount() < MIN_MINE_COUNT || newGameRequest.getMinesCount() > maxMineCount) {
            throw new BadRequestException("Количество мин должно быть не менее 1 и не более " + maxMineCount);
        }
    }

    private int countMaxMine(NewGameRequest newGameRequest) {
        return newGameRequest.getHeight() * newGameRequest.getWidth() - 1;
    }

}
