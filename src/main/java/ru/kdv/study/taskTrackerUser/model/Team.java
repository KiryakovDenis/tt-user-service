package ru.kdv.study.taskTrackerUser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Team {
    private Long id;
    private String name;
    private Long ownerId;
    private List<Long> members;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
