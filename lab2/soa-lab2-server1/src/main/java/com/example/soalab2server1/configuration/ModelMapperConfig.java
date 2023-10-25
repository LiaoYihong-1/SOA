package com.example.soalab2server1.configuration;

import com.example.soalab2server1.dao.model.Position;
import com.example.soalab2server1.dao.model.Worker;
import com.example.soalab2server1.dao.model.WorkerFullInfo;
import com.example.soalab2server1.dao.request.CreateWorkerRequest;
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
import java.time.ZonedDateTime;
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
        mapCreateWorkerRequestToWorker(mapper);
        return mapper;
    }

    private static void mapCreateWorkerRequestToWorker(ModelMapper mapper) {
        TypeMap<CreateWorkerRequest, Worker> createWorkerRequestWorker = mapper
                .createTypeMap(CreateWorkerRequest.class, Worker.class);
        Converter<LocalDate, LocalDateTime> localDateToLocalDateTime = c -> c.getSource().atStartOfDay();
        createWorkerRequestWorker.addMappings(mapping -> {
            mapping.using(localDateToLocalDateTime).map(
                    CreateWorkerRequest::getStartDate,
                    Worker::setStartDate
            );
        });
    }

    private static void mapWorkerInfoToWorker(
            ModelMapper mapper
    ) throws IllegalArgumentException {
        TypeMap<WorkerInfo, Worker> workerInfoWorker = mapper
                .createTypeMap(WorkerInfo.class, Worker.class);

        Converter<LocalDate, LocalDateTime> localDateToLocalDateTime = c -> c.getSource().atStartOfDay();
        Converter<String, Position> stringToSTRING = c -> Position.valueOf(c.getSource().toUpperCase());

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
        Converter<ZonedDateTime, LocalDateTime> zonedDateTimeToLocalDateTime = c ->c.getSource()
                .toLocalDateTime().withNano(0);

        workerInfoWorker.addMappings(mapping -> {
            mapping.using(localDateToLocalDateTime).map(
                    Worker::getStartDate,
                    WorkerFullInfo::setStartDate
            );
            mapping.using(zonedDateTimeToLocalDateTime).map(
                    Worker::getCreationDate,
                    WorkerFullInfo::setCreationDate
            );
        });

    }
}
