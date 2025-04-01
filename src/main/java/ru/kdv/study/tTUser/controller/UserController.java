package ru.kdv.study.tTUser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kdv.study.tTUser.model.dto.UserDeleteResponse;
import ru.kdv.study.tTUser.model.dto.UserInsert;
import ru.kdv.study.tTUser.model.dto.UserResponse;
import ru.kdv.study.tTUser.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Создать пользователя")
    public UserResponse create(@RequestBody final UserInsert userInsert) {
        return userService.create(userInsert);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по id")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Получить список пользователей")
    public List<UserResponse> getAllActive() {
        return userService.getAllActive();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя")
    public UserDeleteResponse delete(@PathVariable Long id) {
        return userService.delete(id);
    }
}