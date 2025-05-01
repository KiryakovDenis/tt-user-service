package ru.kdv.study.taskTrackerUser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.kdv.study.taskTrackerUser.model.Role;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private Role role;
}
