package ru.kdv.study.tTUser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kdv.study.tTUser.exception.BadRequestException;
import ru.kdv.study.tTUser.model.User;
import ru.kdv.study.tTUser.model.dto.UserDeleteResponse;
import ru.kdv.study.tTUser.model.dto.UserInsert;
import ru.kdv.study.tTUser.model.dto.UserResponse;
import ru.kdv.study.tTUser.repository.UserRepository;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public UserResponse create(final UserInsert userInsert) {
        validate(userInsert);
        return userToResponseUser(
                userRepository.insert(userInsertToUser(userInsert))
        );
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return userToResponseUser(
                userRepository.getById(id)
        );
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllActive() {
        return userRepository.getAllActive().stream()
                .map(this::userToResponseUser)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDeleteResponse delete(Long id) {
        userRepository.delete(id);
        return new UserDeleteResponse(true);
    }

    private User userInsertToUser(UserInsert userInsert) {
        return User.builder()
                .username(userInsert.getUsername())
                .passwordHash(makeHashPassword(userInsert.getPassword()))
                .build();
    }

    private UserResponse userToResponseUser(User user) {
        return new UserResponse(user.getId(), user.getUsername());
    }

    private String makeHashPassword(String password) {
        try {
            return DatatypeConverter.printHexBinary(
                        MessageDigest.getInstance("MD5").digest(password.getBytes())
                    ).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw BadRequestException.create("Ошибка алгоритма шифрования пароля");
        }
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