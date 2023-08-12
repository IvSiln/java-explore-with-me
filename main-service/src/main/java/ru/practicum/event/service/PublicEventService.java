package ru.practicum.event.service;

import ru.practicum.event.dto.EventFilterParamsDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventService {

    List<EventShortDto> getEventsByPublic(EventFilterParamsDto params, HttpServletRequest request);

    EventFullDto getEventsByPublic(Long eventId, HttpServletRequest request);
}

