package ru.practicum.stats;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.stats.dto.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.practicum.stats.dto.TimeStampConverter.mapToString;
import static ru.practicum.util.StatsClientConstants.*;

@Service
public class StatsClient extends BaseClient {

    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(serverUrl, builder);
    }

    public ResponseEntity<Object> createHit(String app, String uri, String ip, LocalDateTime timestamp) {
        EndpointHit endpointHit = new EndpointHit(app, uri, ip, mapToString(timestamp));
        return makeAndSendRequest(HttpMethod.POST, HIT_ENDPOINT, null, endpointHit);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start,
                                           LocalDateTime end,
                                           @Nullable List<String> uris,
                                           @Nullable Boolean unique) {
        Map<String, Object> parameters = getTimeLimitParameters(start, end);
        String path = getPath(uris, unique);
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    private Map<String, Object> getTimeLimitParameters(LocalDateTime start, LocalDateTime end) {
        if (isNull(start) || isNull(end) || end.isBefore(start)) {
            throw new BadRequestException(INCORRECT_TIME_LIMIT_EXCEPTION_INFO);
        }
        return Map.of(
                "start", mapToString(start),
                "end", mapToString(end)
        );
    }

    private String getPath(List<String> uris, Boolean unique) {
        StringJoiner pathBuilder = new StringJoiner("&", GET_STATS_ENDPOINT + START_END_PATH_PART,
                "");
        if (nonNull(uris) && !uris.isEmpty()) {
            uris.forEach(uri -> pathBuilder.add(URIS_PATH_PART + uri));
        }
        if (nonNull(unique)) {
            pathBuilder.add(UNIQUE_PATH_PART + unique);
        }
        return pathBuilder.toString();
    }
}

