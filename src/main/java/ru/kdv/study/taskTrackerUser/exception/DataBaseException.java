package ru.kdv.study.taskTrackerUser.exception;

public class DataBaseException extends RuntimeException {

    public static DataBaseException create(String message) {
        return new DataBaseException(message);
    }

    public DataBaseException(String message) {
        super(message);
    }
}
