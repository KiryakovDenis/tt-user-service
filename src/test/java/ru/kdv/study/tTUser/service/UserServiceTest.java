package ru.kdv.study.tTUser.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kdv.study.tTUser.exception.BadRequestException;
import ru.kdv.study.tTUser.model.User;
import ru.kdv.study.tTUser.model.dto.UserInsert;
import ru.kdv.study.tTUser.model.dto.UserResponse;
import ru.kdv.study.tTUser.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private UserInsert validUserInsert = new UserInsert("user1", "password1");
    private User validUserIn = User.builder()
            .id(null)
            .username(validUserInsert.getUsername())
            .passwordHash(validUserInsert.getPassword().hashCode())
            .deleted(false)
            .deletedAt(null)
            .build();
    private User validUserOut = User.builder()
            .id(1L)
            .username(validUserInsert.getUsername())
            .passwordHash(validUserInsert.getPassword().hashCode())
            .deleted(false)
            .deletedAt(null)
            .build();
    private UserResponse validUserResponse = new UserResponse(validUserOut.getId(), validUserIn.getUsername());

    @Test
    @DisplayName("Успешное создание пользователя")
    public void SuccessCreateUser() {
        Mockito.when(userRepository.insert(validUserIn)).thenReturn(validUserOut);

        UserResponse result = userService.create(validUserInsert);

        assertThat(result).isEqualTo(validUserResponse);
        verify(userRepository).insert(validUserIn);
    }

    private UserInsert NullUsernameUserInsert = new UserInsert(null, "password1");

    @Test
    @DisplayName("Валидация имени пользователя со значением null")
    public void validateNullUsername() {
        BadRequestException bre = assertThrows(BadRequestException.class, () -> userService.create(NullUsernameUserInsert));
    }

    private UserInsert EmptyUsernameUserInsert = new UserInsert(null, "password1");

    @Test
    @DisplayName("Валидация имени пользователя с пустым значением")
    public void validateEmptyUsername() {
        BadRequestException bre = assertThrows(BadRequestException.class, () -> userService.create(EmptyUsernameUserInsert));
    }

    private UserInsert NullPasswordUserInsert = new UserInsert("user1", null);

    @Test
    @DisplayName("Валидация имени пользователя со значением null")
    public void validateNullPassword() {
        BadRequestException bre = assertThrows(BadRequestException.class, () -> userService.create(NullPasswordUserInsert));
    }

    private UserInsert EmptyPasswordUserInsert = new UserInsert("user1", null);

    @Test
    @DisplayName("Валидация имени пользователя с пустым значением")
    public void validateEmptyPassword() {
        BadRequestException bre = assertThrows(BadRequestException.class, () -> userService.create(EmptyPasswordUserInsert));
    }
}