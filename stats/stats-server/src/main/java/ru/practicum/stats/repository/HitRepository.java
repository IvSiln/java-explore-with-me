package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.model.Hit;
import ru.practicum.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    // Получение статистики с учетом уникальных IP по временному промежутку и URI
    @Query(value = "select new ru.practicum.stats.model.Stats(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit h " +
            "where h.timestamp between ?1 and ?2 and h.uri in ?3 " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<Stats> getUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    // Получение статистики без учета уникальных IP по временному промежутку и URI
    @Query(value = "select new ru.practicum.stats.model.Stats(h.app, h.uri, count(h.ip)) " +
            "from Hit h " +
            "where h.timestamp between ?1 and ?2 and h.uri in ?3 " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<Stats> getNotUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    // Получение статистики с учетом уникальных IP по временному промежутку без URI
    @Query(value = "select new ru.practicum.stats.model.Stats(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<Stats> getUniqueStatsWithoutUris(LocalDateTime start, LocalDateTime end);

    // Получение статистики без учета уникальных IP по временному промежутку без URI
    @Query(value = "select new ru.practicum.stats.model.Stats(h.app, h.uri, count(h.ip)) " +
            "from Hit h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<Stats> getNotUniqueStatsWithoutUris(LocalDateTime start, LocalDateTime end);
}
