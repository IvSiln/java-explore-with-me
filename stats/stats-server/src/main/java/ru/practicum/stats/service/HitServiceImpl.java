package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.exception.BadRequestException;
import ru.practicum.stats.mapper.HitMapper;
import ru.practicum.stats.model.Stats;
import ru.practicum.stats.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;

    @Override
    @Transactional
    public void createHit(HitDto hitDto) {
        hitRepository.save(HitMapper.toHit(hitDto));
    }

    @Override
    public List<Stats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        validateDateOrder(start, end);
        return !uris.isEmpty()
                ? hitRepository.getNotUniqueStats(start, end, uris)
                : hitRepository.getNotUniqueStatsWithoutUris(start, end);
    }

    @Override
    public List<Stats> getUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        validateDateOrder(start, end);
        return !uris.isEmpty()
                ? hitRepository.getUniqueStats(start, end, uris)
                : hitRepository.getUniqueStatsWithoutUris(start, end);

    }

    private void validateDateOrder(LocalDateTime start, LocalDateTime end) {
        if (isNull(start) || isNull(end) || end.isBefore(start)) {
            throw new BadRequestException("Дата конца не может быть перед датой начала.");
        }
    }
}
