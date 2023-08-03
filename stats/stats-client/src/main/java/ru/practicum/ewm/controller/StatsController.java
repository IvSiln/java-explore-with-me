package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsClient statsClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> create(@RequestBody EndpointHitDto endpointHitDto) {
        return statsClient.create(endpointHitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> find(@RequestParam("start") LocalDateTime start,
                                       @RequestParam("end") LocalDateTime end,
                                       @RequestParam(value = "uris", required = false) List<String> uris,
                                       @RequestParam(value = "isUnique", defaultValue = "false") boolean isUnique) {
        return statsClient.find(start, end, uris, isUnique);
    }
}
