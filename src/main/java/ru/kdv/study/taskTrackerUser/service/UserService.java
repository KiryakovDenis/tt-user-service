package ru.kdv.study.taskTrackerUser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kdv.study.taskTrackerUser.exception.BadRequestException;
import ru.kdv.study.taskTrackerUser.model.User;
import ru.kdv.study.taskTrackerUser.model.dto.UserDeleteResponse;
import ru.kdv.study.taskTrackerUser.model.dto.UserInsert;
import ru.kdv.study.taskTrackerUser.model.dto.UserResponse;
import ru.kdv.study.taskTrackerUser.repository.UserRepository;
import ru.kdv.study.taskTrackerUser.security.securityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TaskService taskService;

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
    public List<UserResponse> getActiveByIds(List<Long> ids) {
        return userRepository.getActiveByIds(ids).stream()
                .map(this::userToResponseUser)
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public UserDeleteResponse delete(Long id) {
        validateDelete(id);
        userRepository.delete(id);
        return new UserDeleteResponse(true);
    }

    private void validateDelete(Long id){
        if (taskService.checkActualTask(id)) {
            throw BadRequestException.create("Пользователя невозможно удалить, поскольку существуют связанные актуальные задачи");
        }
    }

    private User userInsertToUser(UserInsert userInsert) {
        return User.builder()
                .username(userInsert.getUsername())
                .passwordHash(securityUtil.makeHashPassword(userInsert.getPassword()))
                .role(userInsert.getRole())
                .build();
    }

    private UserResponse userToResponseUser(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
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