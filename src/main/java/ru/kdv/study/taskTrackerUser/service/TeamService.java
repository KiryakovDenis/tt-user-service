package ru.kdv.study.taskTrackerUser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.kdv.study.taskTrackerUser.exception.BadRequestException;
import ru.kdv.study.taskTrackerUser.model.Role;
import ru.kdv.study.taskTrackerUser.model.Team;
import ru.kdv.study.taskTrackerUser.model.dto.TeamInsert;
import ru.kdv.study.taskTrackerUser.repository.TeamRepository;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final MemberService memberService;
    private final UserService userService;

    public Team create(TeamInsert team) {
        Team temp = TeamInsertToTeam(team);
        validate(temp);
        return teamRepository.insert(temp);
    }

    public Team getById(Long id){
        Team result = teamRepository.getById(id);
        result.setMembers(memberService.findMembers(id));
        return result;
    }

    private Team TeamInsertToTeam(TeamInsert team) {
        return Team.builder()
                .name(team.getName())
                .ownerId(team.getOwnerId())
                .build();
    }

    private void validate(Team team) {
        if (!StringUtils.hasText(team.getName())) {
            throw BadRequestException.create("Не указано имя команды");
        }

        if (team.getOwnerId() == null) {
            throw BadRequestException.create("Не указан владелец команды");
        }

        if (!Role.MANAGER.equals(userService.getById(team.getOwnerId()).getRole())) {
            throw BadRequestException.create("Владельцем команды может быть только пользователь с ролью \"MANAGER\".");
        }
    }
}