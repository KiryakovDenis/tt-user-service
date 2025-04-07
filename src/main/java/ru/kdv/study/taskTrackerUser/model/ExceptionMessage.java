package ru.kdv.study.taskTrackerUser.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionMessage {
    private boolean success;
    private String message;
}
