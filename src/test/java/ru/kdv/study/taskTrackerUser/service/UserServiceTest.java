package ru.kdv.study.taskTrackerUser.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kdv.study.taskTrackerUser.exception.BadRequestException;
import ru.kdv.study.taskTrackerUser.model.Role;
import ru.kdv.study.taskTrackerUser.model.User;
import ru.kdv.study.taskTrackerUser.model.dto.UserInsert;
import ru.kdv.study.taskTrackerUser.model.dto.UserResponse;
import ru.kdv.study.taskTrackerUser.repository.UserRepository;
import ru.kdv.study.taskTrackerUser.security.securityUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private UserInsert validUserInsert = new UserInsert("user1", "password1", Role.MANAGER);
    private User validUserIn = User.builder()
            .id(null)
            .username(validUserInsert.getUsername())
            .passwordHash(securityUtil.makeHashPassword(validUserInsert.getPassword()))
            .deleted(false)
            .deletedAt(null)
            .role(validUserInsert.getRole())
            .build();
    private User validUserOut = User.builder()
            .id(1L)
            .username(validUserInsert.getUsername())
            .passwordHash(securityUtil.makeHashPassword(validUserInsert.getPassword()))
            .deleted(false)
            .deletedAt(null)
            .role(validUserInsert.getRole())
            .build();
    private UserResponse validUserResponse = new UserResponse(validUserOut.getId(), validUserIn.getUsername(), validUserOut.getRole());
    private UserInsert NullUsernameUserInsert = new UserInsert(null, "password1", Role.USER);
    private UserInsert EmptyUsernameUserInsert = new UserInsert(null, "password1", Role.USER);
    private UserInsert NullPasswordUserInsert = new UserInsert("user1", null, Role.USER);
    private UserInsert EmptyPasswordUserInsert = new UserInsert("user1", null, Role.USER);

    @Test
    @DisplayName("Успешное создание пользователя")
    public void SuccessCreateUser() {
        Mockito.when(userRepository.insert(validUserIn)).thenReturn(validUserOut);

        UserResponse result = userService.create(validUserInsert);

        assertThat(result).isEqualTo(validUserResponse);
        verify(userRepository).insert(validUserIn);
    }

    @Test
    @DisplayName("Валидация имени пользователя со значением null")
    public void validateNullUsername() {
        String errorMessage = "Не заполнено поле username";
        BadRequestException bre = assertThrows(BadRequestException.class, () -> userService.create(NullUsernameUserInsert));
        assertThat(bre.getMessage()).isEqualTo(errorMessage);

    }

    @Test
    @DisplayName("Валидация имени пользователя с пустым значением")
    public void validateEmptyUsername() {
        String errorMessage = "Не заполнено поле username";
        BadRequestException bre = assertThrows(BadRequestException.class, () -> userService.create(EmptyUsernameUserInsert));
        assertThat(bre.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("Валидация пароля пользователя со значением null")
    public void validateNullPassword() {
        String errorMessage = "Не заполнено поле password";
        BadRequestException bre = assertThrows(BadRequestException.class, () -> userService.create(NullPasswordUserInsert));
        assertThat(bre.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("Валидация пароля пользователя с пустым значением")
    public void validateEmptyPassword() {
        String errorMessage = "Не заполнено поле password";
        BadRequestException bre = assertThrows(BadRequestException.class, () -> userService.create(EmptyPasswordUserInsert));
        assertThat(bre.getMessage()).isEqualTo(errorMessage);
    }
}