package ru.kdv.study.taskTrackerUser.exception;

public class ExternalServiceException extends RuntimeException{
    public static ExternalServiceException create(String message) {
        return new ExternalServiceException(message);
    }
    
    public ExternalServiceException(String message) {
        super(message);
    }
}
