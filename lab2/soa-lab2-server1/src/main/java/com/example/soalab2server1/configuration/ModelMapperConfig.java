package com.example.soalab2server1.configuration;

import com.example.soalab2server1.dao.model.Worker;
import com.example.soalab2server1.dao.model.WorkerFullInfo;
import com.example.soalab2server1.dao.request.WorkerInfo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapperBean() {

        ModelMapper mapper = new ModelMapper();

        mapper
                .getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        mapWorkerInfoToWorker(mapper);
        mapWorkerToWorkerFullInfo(mapper);

        return mapper;
    }

    private static void mapWorkerInfoToWorker(
            ModelMapper mapper
    ) {
        TypeMap<WorkerInfo, Worker> workerInfoWorker = mapper
                .createTypeMap(WorkerInfo.class, Worker.class);

        Converter<LocalDate, LocalDateTime> localDateToLocalDateTime = c -> c.getSource().atStartOfDay();
        Converter<String, String> stringToSTRING = c -> c.getSource().toUpperCase();

        workerInfoWorker.addMappings(mapping -> {
            mapping.using(localDateToLocalDateTime).map(
                    WorkerInfo::getStartDate,
                    Worker::setStartDate
            );
            mapping.using(stringToSTRING).map(
                    WorkerInfo::getPosition,
                    Worker::setPosition
            );
        });
    }

    private static void mapWorkerToWorkerFullInfo(
            ModelMapper mapper
    ) {
        TypeMap<Worker, WorkerFullInfo> workerInfoWorker = mapper
                .createTypeMap(Worker.class, WorkerFullInfo.class);

        Converter<LocalDateTime, LocalDate> localDateToLocalDateTime = c -> c.getSource().toLocalDate();

        workerInfoWorker.addMappings(mapping -> {
            mapping.using(localDateToLocalDateTime).map(
                    Worker::getStartDate,
                    WorkerFullInfo::setStartDate
            );
        });

    }
}
