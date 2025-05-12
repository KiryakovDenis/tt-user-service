package ru.kdv.study.taskTrackerUser.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kdv.study.taskTrackerUser.exception.BadRequestException;
import ru.kdv.study.taskTrackerUser.model.Role;
import ru.kdv.study.taskTrackerUser.model.User;
import ru.kdv.study.taskTrackerUser.model.dto.MemberInsert;
import ru.kdv.study.taskTrackerUser.model.dto.UserResponse;
import ru.kdv.study.taskTrackerUser.repository.MemberRepository;
import ru.kdv.study.taskTrackerUser.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MemberService memberService;

    private MemberInsert memberInsert;
    private User manager;
    private User user;

    @BeforeEach
    public void setUp() {
        memberInsert = new MemberInsert(1L,2L,3L);

        manager = User.builder()
                .id(3L)
                .username("username")
                .role(Role.MANAGER)
        .build();

        user = User.builder()
                .id(1L)
                .username("user")
                .role(Role.MANAGER)
                .build();
    }

    @Test
    public void testAddUserToTeam() {
        doNothing().when(memberRepository).addUserToTeam(1L, 2L);
        when(userService.getById(3L)).thenReturn(new UserResponse(manager.getId(), manager.getUsername(), manager.getRole()));

        assertDoesNotThrow(() -> memberService.addUserToTeam(memberInsert));

        verify(memberRepository, times(1)).addUserToTeam(1L, 2L);
    }

    @Test
    public void testAddUserToTeamWithNullUserId() {
        memberInsert.setUserId(null);

        assertThrows(BadRequestException.class, () -> memberService.addUserToTeam(memberInsert));
    }

    @Test
    public void testAddUserToTeamWithNonManagerEditor() {
        manager.setRole(Role.USER);
        when(userService.getById(anyLong())).thenReturn(new UserResponse(manager.getId(), manager.getUsername(), manager.getRole()));

        assertThrows(BadRequestException.class, () -> memberService.addUserToTeam(memberInsert));
    }

    @Test
    public void testAddUserToTeamWithNullTeamId() {
        memberInsert.setTeamId(null);
        when(userService.getById(3L)).thenReturn(new UserResponse(manager.getId(), manager.getUsername(), manager.getRole()));

        assertThrows(BadRequestException.class, () -> memberService.addUserToTeam(memberInsert));
    }

    @Test
    public void testRemoveUserFromTeam() {
        doNothing().when(memberRepository).removeUserFromTeam(anyLong(), anyLong());
        when(userService.getById(anyLong())).thenReturn(new UserResponse(manager.getId(), manager.getUsername(), manager.getRole()));

        assertDoesNotThrow(() -> memberService.removeUserFromTeam(memberInsert));

        verify(memberRepository, times(1)).removeUserFromTeam(1L, 2L);
    }

    @Test
    public void testRemoveUserFromTeamWithNullUserId() {
        memberInsert.setUserId(null);

        assertThrows(BadRequestException.class, () -> memberService.removeUserFromTeam(memberInsert));
    }

    @Test
    public void testRemoveUserFromTeamWithNonManagerEditor() {
        manager.setRole(Role.USER);
        when(userService.getById(anyLong())).thenReturn(new UserResponse(manager.getId(), manager.getUsername(), manager.getRole()));

        assertThrows(BadRequestException.class, () -> memberService.removeUserFromTeam(memberInsert));
    }

    @Test
    public void testRemoveUserFromTeamWithNullTeamId() {
        memberInsert.setTeamId(null);
        when(userService.getById(anyLong())).thenReturn(new UserResponse(manager.getId(), manager.getUsername(), manager.getRole()));
        assertThrows(BadRequestException.class, () -> memberService.removeUserFromTeam(memberInsert));
    }

    @Test
    public void testFindUsersByTeam() {
        List<User> users = Arrays.asList(User.builder().id(1L).username("username").role(Role.USER).build(),
                                         User.builder().id(2L).username("username1").role(Role.USER).build());
        when(userRepository.findUsersByTeam(anyLong())).thenReturn(users);

        List<User> result = memberService.findUsersByTeam(2L);

        assertEquals(users, result);
        verify(userRepository, times(1)).findUsersByTeam(2L);
    }

    @Test
    public void testFindUsersByTeamWithNullTeamId() {
        assertThrows(BadRequestException.class, () -> memberService.findUsersByTeam(null));
    }

    @Test
    public void testFindMembers() {
        List<Long> members = Arrays.asList(1L, 2L);
        when(memberRepository.findMembers(anyLong())).thenReturn(members);

        List<Long> result = memberService.findMembers(2L);

        assertEquals(members, result);
        verify(memberRepository, times(1)).findMembers(2L);
    }
}
