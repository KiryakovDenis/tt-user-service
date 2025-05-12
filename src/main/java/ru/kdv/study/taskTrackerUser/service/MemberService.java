package ru.kdv.study.taskTrackerUser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kdv.study.taskTrackerUser.exception.BadRequestException;
import ru.kdv.study.taskTrackerUser.model.Role;
import ru.kdv.study.taskTrackerUser.model.User;
import ru.kdv.study.taskTrackerUser.model.dto.MemberInsert;
import ru.kdv.study.taskTrackerUser.repository.MemberRepository;
import ru.kdv.study.taskTrackerUser.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    public final MemberRepository memberRepository;
    public final UserRepository userRepository;
    public final UserService userService;

    public void addUserToTeam(MemberInsert memberInsert) {
        validate(memberInsert);
        memberRepository.addUserToTeam(memberInsert.getUserId(), memberInsert.getTeamId());
    }

    public void removeUserFromTeam(MemberInsert memberInsert) {
        validate(memberInsert);
        memberRepository.removeUserFromTeam(memberInsert.getUserId(), memberInsert.getTeamId());
    }

    public List<User> findUsersByTeam(Long teamId) {
        validate(teamId);
        return userRepository.findUsersByTeam(teamId);
    }

    public List<Long> findMembers(Long teamId) {
        return memberRepository.findMembers(teamId);
    }

    private void validate(MemberInsert memberInsert) {
        if (memberInsert.getUserId() == null) {
            throw BadRequestException.create("Не указан id пользователя");
        }

        if (!Role.MANAGER.equals(userService.getById(memberInsert.getEditorId()).getRole())) {
            throw BadRequestException.create("Нет прав редактирование списка участников команды");
        }

        if (memberInsert.getTeamId() == null) {
            throw BadRequestException.create("Не указан id команды");
        }
    }

    public void validate(Long teamId) {
        if (teamId == null) {
            throw BadRequestException.create("Не указан id команды");
        }
    }
}