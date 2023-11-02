package sanhak.shserver.cad.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sanhak.shserver.ErrorResult;
import sanhak.shserver.cad.CadController;

@Slf4j
@RestControllerAdvice(assignableTypes = CadController.class)
public class CadControllerAdvice {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class, InterruptedException.class})
    public ErrorResult internalExHandle(RuntimeException e) {
        log.error(e.getMessage());
        return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(RuntimeException.class)
//    public ErrorResult internalExHandle(RuntimeException e) {
//        log.error(e.getMessage());
//        return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
//    }
}
