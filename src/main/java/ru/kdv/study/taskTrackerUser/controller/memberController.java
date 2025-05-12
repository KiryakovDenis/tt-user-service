package ru.kdv.study.taskTrackerUser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kdv.study.taskTrackerUser.model.User;
import ru.kdv.study.taskTrackerUser.model.dto.MemberInsert;
import ru.kdv.study.taskTrackerUser.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/member")
@Tag(name = "Участники команд")
@RequiredArgsConstructor
public class memberController {

    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "Получить список участников команды")
    public List<User> getMembers(@RequestParam Long teamId) {
        return memberService.findUsersByTeam(teamId);
    }

    @PostMapping
    @Operation(summary = "Добавить участника в команду")
    public void addMember(@RequestBody MemberInsert memberInsert) {
        memberService.addUserToTeam(memberInsert);
    }

    @PatchMapping
    @Operation(summary = "Удалить участника из команды")
    public void removeMember(@RequestBody MemberInsert memberInsert) {
        memberService.removeUserFromTeam(memberInsert);
    }
}