package ru.kdv.study.taskTrackerUser.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kdv.study.taskTrackerUser.model.ExceptionMessage;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionMessage> handleException(Exception e) {
        log.error("ExceptionController#Exception", e);
        return ResponseEntity.internalServerError().body(new ExceptionMessage(false, e.getMessage()));
    }

    @ExceptionHandler(DataBaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionMessage> handleDataBaseException(DataBaseException e) {
        log.error("ExceptionController#DataBaseException", e);
        return ResponseEntity.internalServerError().body(new ExceptionMessage(false, e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionMessage> handleBadRequestException(BadRequestException e) {
        log.error("ExceptionController#BadRequestException", e);
        return ResponseEntity.badRequest().body(new ExceptionMessage(false, e.getMessage()));
    }

    @ExceptionHandler(ExternalServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionMessage> handleBadRequestException(ExternalServiceException e) {
        log.error("ExceptionController#BnExternalServiceException", e);
        return ResponseEntity.internalServerError().body(new ExceptionMessage(false, e.getMessage()));
    }
}