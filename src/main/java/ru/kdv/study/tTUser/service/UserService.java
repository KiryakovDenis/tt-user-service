package ru.kdv.study.tTUser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.kdv.study.tTUser.exception.BadRequestException;
import ru.kdv.study.tTUser.model.User;
import ru.kdv.study.tTUser.model.dto.UserDeleteResponse;
import ru.kdv.study.tTUser.model.dto.UserInsert;
import ru.kdv.study.tTUser.model.dto.UserResponse;
import ru.kdv.study.tTUser.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse create(final UserInsert userInsert) {
        validate(userInsert);
        return UserToResponseUser(
                userRepository.insert(UserInsertToUser(userInsert))
        );
    }

    public UserResponse getById(Long id) {
        return UserToResponseUser(
                userRepository.getById(id)
        );
    }

    public List<UserResponse> getAllActive() {
        return userRepository.getAllActive().stream()
                .map(this::UserToResponseUser)
                .toList();
    }

    public UserDeleteResponse delete(Long id) {
        userRepository.delete(id);
        return new UserDeleteResponse(true);
    }

    private User UserInsertToUser (UserInsert userInsert) {
        return User.builder()
                .username(userInsert.getUsername())
                .passwordHash(userInsert.getPassword().hashCode())
                .build();
    }

    private UserResponse UserToResponseUser(User user) {
        return new UserResponse(user.getId(), user.getUsername());
    }

    private void validate(UserInsert userInsert) {

        if (!StringUtils.hasText(userInsert.getUsername())) {
            throw BadRequestException.create("Не заполнено поле username");
        }

        if (!StringUtils.hasText(userInsert.getPassword())) {
            throw BadRequestException.create("Не заполнено поле password");
        }
    }
}