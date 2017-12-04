package by.bstu.feis.ii12.controller;

import by.bstu.feis.ii12.exception.NoWordException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class TranslateExceptionHandler {

    @ExceptionHandler(NoWordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleNoWordException(NoWordException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("status", "error");
        response.put("reason", ex.getLocalizedMessage());
        response.put("word", ex.getWord());

        return response;
    }

}
