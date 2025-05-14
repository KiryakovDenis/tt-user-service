package ru.kdv.study.taskTrackerUser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kdv.study.taskTrackerUser.model.Team;
import ru.kdv.study.taskTrackerUser.model.dto.TeamInsert;
import ru.kdv.study.taskTrackerUser.service.TeamService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")
@Tag(name = "Команда")
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить команду по id")
    public Team getById(@PathVariable Long id){
        return teamService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Создать команду")
    public Team create(@RequestBody TeamInsert teamInsert) {
        return teamService.create(teamInsert);
    }

}
