package com.test.noteservice.controller;

import com.test.noteservice.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({UserNotFoundException.class, NoteNotFoundException.class, UsernameExistsException.class,
            LikeException.class, UnlikeException.class})
    public ResponseEntity<Object> handleExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}