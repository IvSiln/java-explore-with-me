package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.WatchStatsDto;
import ru.practicum.ewm.model.WatchStats;

public class WatchStatsMapper {

    public static WatchStats toWatchStats(WatchStatsDto watchStatsDto) {
        return new WatchStats(watchStatsDto.getApp(),
                watchStatsDto.getUri(),
                watchStatsDto.getHits());
    }

    public static WatchStatsDto toWatchStatsDto(WatchStats watchStats) {
        return new WatchStatsDto(watchStats.getApp(),
                watchStats.getUri(),
                watchStats.getHits());
    }
}

