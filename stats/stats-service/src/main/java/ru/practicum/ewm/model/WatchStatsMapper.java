package ru.practicum.ewm.model;

import ru.practicum.ewm.dto.WatchStatsDto;

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

