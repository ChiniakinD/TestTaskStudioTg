package com.chiniakin.configuration;

import com.chiniakin.entity.Game;
import com.chiniakin.model.NewGameResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настройки ModelMapper.
 *
 * @author ChiniakinD
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<Game, NewGameResponse>() {
            @Override
            protected void configure() {
                map(source.getId(), destination.getGameId());
                map(source.getField().getPlayerCells(), destination.getField());
            }
        });

        return modelMapper;
    }

}
