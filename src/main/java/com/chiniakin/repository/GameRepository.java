package com.chiniakin.repository;

import com.chiniakin.entity.Game;
import com.chiniakin.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {

    /**
     * Находит игру по её id, иначен выбрасывает исключение.
     *
     * @param id id игры.
     * @return игра.
     */
    default Game getGameByIdOrThrow(UUID id) {
        return findById(id).orElseThrow(() -> new NotFoundException("Игра с таким id не найдена"));
    }

}
