package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.WatchStatsDto;
import ru.practicum.ewm.model.EndpointHitMapper;
import ru.practicum.ewm.model.WatchStats;
import ru.practicum.ewm.model.WatchStatsMapper;
import ru.practicum.ewm.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public EndpointHitDto create(EndpointHitDto endpointHitDto) {
        statsRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
        return endpointHitDto;
    }

    @Override
    public List<WatchStatsDto> find(LocalDateTime start,
                                    LocalDateTime end,
                                    List<String> uris,
                                    boolean unique) {

        List<WatchStats> viewStatsList = new ArrayList<>();

        if (uris != null) {
            for (String uri : uris) {
                if (unique) {
                    viewStatsList.add(statsRepository.findUniqueHits(uri, start, end));
                } else {
                    viewStatsList.add(statsRepository.findHits(uri, start, end));
                }
            }
        }

        return viewStatsList.stream()
                .sorted(Comparator.comparing(WatchStats::getHits).reversed())
                .map(WatchStatsMapper::toWatchStatsDto)
                .collect(Collectors.toList());
    }

}
