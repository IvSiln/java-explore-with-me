package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.user.dto.UserWithSubDto;
import ru.practicum.user.service.UserServiceImpl;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserPublicController {

    private final UserServiceImpl userService;

    @GetMapping(value = "/{userId}")
    public UserWithSubDto get(@PathVariable Long userId) {
        return userService.getUsersWithSubscriptions(userId);
    }
}
