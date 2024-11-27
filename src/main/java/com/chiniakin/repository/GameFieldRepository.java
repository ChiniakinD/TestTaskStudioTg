package com.chiniakin.repository;

import com.chiniakin.entity.GameField;
import com.chiniakin.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameFieldRepository extends JpaRepository<GameField, UUID> {

    /**
     * Находит игровое поле по его id, иначен выбрасываетр исключению.
     *
     * @param id id игрового поля.
     * @return игровое поле.
     */
    default GameField getGameFieldByIdOrThrow(UUID id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Игровое поле с таким id не найдено"));
    }

}
