package ru.kdv.study.taskTrackerUser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kdv.study.taskTrackerUser.exception.BadRequestException;
import ru.kdv.study.taskTrackerUser.model.Role;
import ru.kdv.study.taskTrackerUser.model.User;
import ru.kdv.study.taskTrackerUser.model.dto.MemberRequest;
import ru.kdv.study.taskTrackerUser.repository.MemberRepository;
import ru.kdv.study.taskTrackerUser.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    public final MemberRepository memberRepository;
    public final UserRepository userRepository;
    public final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    public void addUserToTeam(MemberRequest memberRequest) {
        validate(memberRequest);
        memberRepository.addUserToTeam(memberRequest.getUserId(), memberRequest.getTeamId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeUserFromTeam(MemberRequest memberRequest) {
        validate(memberRequest);
        memberRepository.removeUserFromTeam(memberRequest.getUserId(), memberRequest.getTeamId());
    }

    @Transactional(readOnly = true)
    public List<User> findUsersByTeam(Long teamId) {
        validate(teamId);
        return userRepository.findUsersByTeam(teamId);
    }

    @Transactional(readOnly = true)
    public List<Long> findMembers(Long teamId) {
        return memberRepository.findMembers(teamId);
    }

    private void validate(MemberRequest memberRequest) {
        if (memberRequest.getUserId() == null) {
            throw BadRequestException.create("Не указан id пользователя");
        }

        if (!Role.MANAGER.equals(userService.getById(memberRequest.getEditorId()).getRole())) {
            throw BadRequestException.create("Нет прав редактирование списка участников команды");
        }

        if (memberRequest.getTeamId() == null) {
            throw BadRequestException.create("Не указан id команды");
        }
    }

    public void validate(Long teamId) {
        if (teamId == null) {
            throw BadRequestException.create("Не указан id команды");
        }
    }
}