package ru.kdv.study.taskTrackerUser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.kdv.study.taskTrackerUser.model.Role;

@Data
@AllArgsConstructor
public class UserInsert {
    private String username;
    private String password;
    private Role role;
}
