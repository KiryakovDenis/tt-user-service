package ru.kdv.study.taskTrackerUser.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kdv.study.taskTrackerUser.exception.BadRequestException;
import ru.kdv.study.taskTrackerUser.model.Role;
import ru.kdv.study.taskTrackerUser.model.Team;
import ru.kdv.study.taskTrackerUser.model.dto.TeamInsert;
import ru.kdv.study.taskTrackerUser.model.dto.UserResponse;
import ru.kdv.study.taskTrackerUser.repository.TeamRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TeamService teamService;

    private TeamInsert teamInsert;
    private Team team;

    @BeforeEach
    public void setUp() {
        teamInsert = new TeamInsert("Test Team", 1L);

        team = Team.builder()
                .name("Test Team")
                .ownerId(1L)
                .build();
    }

    @Test
    public void testCreate() {
        when(teamRepository.insert(any(Team.class))).thenReturn(team);
        when(userService.getById(1L)).thenReturn(new UserResponse(1L, "user", Role.MANAGER));


        Team result = teamService.create(teamInsert);


        assertEquals(team, result);
        verify(teamRepository, times(1)).insert(team);
    }

    @Test
    public void testCreateWithNullName() {
        teamInsert.setName(null);

        assertThrows(BadRequestException.class, () -> teamService.create(teamInsert));
    }

    @Test
    public void testCreateWithNullOwnerId() {
        teamInsert.setOwnerId(null);

        assertThrows(BadRequestException.class, () -> teamService.create(teamInsert));
    }

    @Test
    public void testCreateWithNonManagerOwner() {
        when(userService.getById(anyLong())).thenReturn(new UserResponse(1L, "user", Role.USER));

        assertThrows(BadRequestException.class, () -> teamService.create(teamInsert));
    }

    @Test
    public void testGetById() {
        List<Long> members = Arrays.asList(1L, 2L);
        when(teamRepository.getById(anyLong())).thenReturn(team);
        when(memberService.findMembers(anyLong())).thenReturn(members);

        Team result = teamService.getById(1L);

        assertEquals(team, result);
        assertEquals(members, result.getMembers());
        verify(teamRepository, times(1)).getById(1L);
        verify(memberService, times(1)).findMembers(1L);
    }

    @Test
    public void testGetByIdWithNullId() {
        when(teamRepository.getById(null)).thenThrow(new BadRequestException(""));
        assertThrows(BadRequestException.class, () -> teamService.getById(null));
    }
}