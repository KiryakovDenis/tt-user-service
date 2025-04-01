package ru.kdv.study.tTUser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInsert {
    private String username;
    private String password;
}
