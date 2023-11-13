package sanhak.shserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sanhak.shserver.cad.CadController;

@Slf4j
@RestControllerAdvice(assignableTypes = CadController.class)
public class GlobalControllerAdvice {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class, InterruptedException.class})
    public ErrorResult internalExHandle(RuntimeException e) {
        log.error(e.getMessage());
        return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult badRequestExHandle(IllegalArgumentException e) {
        log.error(e.getMessage());
        return new ErrorResult(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
