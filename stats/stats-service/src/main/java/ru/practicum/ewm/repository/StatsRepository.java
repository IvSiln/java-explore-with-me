package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.WatchStats;

import java.time.LocalDateTime;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.ewm.model.WatchStats(h.app, h.uri, COUNT(h.ip)) FROM EndpointHit h " +
            "WHERE h.uri = ?1 AND (h.timestamp BETWEEN ?2 AND ?3) " +
            "GROUP BY h.app, h.uri, h.ip")
    WatchStats findHits(String uri, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.model.WatchStats(h.app, h.uri, COUNT(DISTINCT h.ip)) FROM EndpointHit h " +
            "WHERE h.uri = ?1 AND (h.timestamp BETWEEN ?2 AND ?3) " +
            "GROUP BY h.app, h.uri, h.ip")
    WatchStats findUniqueHits(String uri, LocalDateTime start, LocalDateTime end);
}
