package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static ru.practicum.utils.ExploreConstantsAndStaticMethods.*;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto newCompDto) {
        List<Event> events = fetchEvents(newCompDto.getEvents());
        Compilation newComp = compilationMapper.toCompilation(newCompDto, events);
        Compilation savedComp = compilationRepository.save(newComp);
        return compilationMapper.toCompilationDto(savedComp);
    }

    @Override
    @Transactional
    public CompilationDto update(Long compId, UpdateCompilationRequest request) {
        Compilation compilation = getCompilationIfExist(compId);
        updateCompilation(compilation, request);
        Compilation updatedComp = compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(updatedComp);
    }

    @Override
    @Transactional
    public void delete(Long compId) {
        Compilation compilation = getCompilationIfExist(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        Page<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageRequestOf(from, size));
        return compilations.map(compilationMapper::toCompilationDto).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto get(Long compId) {
        Compilation compilation = getCompilationIfExist(compId);
        return compilationMapper.toCompilationDto(compilation);
    }

    private Compilation getCompilationIfExist(Long comId) {
        return compilationRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException(COMPILATION_NOT_FOUND));
    }

    private List<Event> fetchEvents(List<Long> eventIds) {
        if (eventIds.isEmpty()) {
            return Collections.emptyList();
        }
        return eventRepository.findAllByIdIn(eventIds);
    }

    private void updateCompilation(Compilation comp, UpdateCompilationRequest request) {
        updateEvents(comp, request.getEvents());
        updatedPinned(comp, request.getPinned());
        updateTitle(comp, request.getTitle());
    }

    private void updateEvents(Compilation comp, List<Long> eventIds) {
        if (!eventIds.isEmpty()) {
            List<Event> updatedEvents = fetchEvents(eventIds);
            comp.setEvents(updatedEvents);
        }
    }

    private void updatedPinned(Compilation comp, Boolean pinned) {
        if (Objects.nonNull(pinned)) {
            comp.setPinned(pinned);
        }
    }

    private void updateTitle(Compilation comp, String newTitle) {
        if (newTitle != null) {
            checkTitleNotUnique(newTitle, comp.getId());
            comp.setTitle(newTitle);
        }
    }

    private void checkTitleNotUnique(String newTitle, Long compId) {
        if (compilationRepository.existsByTitleAndIdNot(newTitle, compId)) {
            throw new ConflictException(COMPILATION_TITLE_ALREADY_EXIST);
        }
    }
}
