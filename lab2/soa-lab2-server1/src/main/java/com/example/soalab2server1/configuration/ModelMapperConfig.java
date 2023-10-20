package com.example.soalab2server1.configuration;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapperBean() {

        ModelMapper mapper = new ModelMapper();

        mapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper;
    }
}