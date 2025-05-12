package ru.kdv.study.taskTrackerUser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberInsert {
    private Long userId;
    private Long teamId;
    private Long editorId;
}
