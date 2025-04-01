package ru.kdv.study.tTUser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    private Integer passwordHash;
    private boolean deleted;
    private LocalDateTime deletedAt;
}
