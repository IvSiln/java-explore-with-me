package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.WatchStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    EndpointHitDto create(EndpointHitDto endpointHitDto);

    List<WatchStatsDto> find(LocalDateTime start,
                            LocalDateTime end,
                            List<String> uris,
                            boolean unique);
}
