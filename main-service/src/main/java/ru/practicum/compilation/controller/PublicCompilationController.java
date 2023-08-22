package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.service.CompilationServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {

    private final CompilationServiceImpl compilationService;

    @GetMapping
    public List<CompilationDto> get(@RequestParam(required = false, defaultValue = "false") Boolean pinned,
                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<CompilationDto> compilations = compilationService.getAll(pinned, from, size);
        log.info("Retrieved {} compilations", compilations.size());
        return compilations;
//        System.out.println(compilationService.getAll(pinned, from, size));
//        return compilationService.getAll(pinned, from, size);
    }

    @GetMapping(value = "/{compId}")
    public CompilationDto get(@PathVariable Long compId) {
        log.info("Getting compilation with ID: {}", compId);
        return compilationService.get(compId);
    }
}
