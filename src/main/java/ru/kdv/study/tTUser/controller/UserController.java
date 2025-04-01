package ru.kdv.study.tTUser.controller;

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
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse create(@RequestBody final UserInsert userInsert) {
        return userService.create(userInsert);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserResponse> getAllActive() {
        return userService.getAllActive();
    }

    @DeleteMapping("/{id}")
    public UserDeleteResponse delete(@PathVariable Long id) {
        return userService.delete(id);
    }
}