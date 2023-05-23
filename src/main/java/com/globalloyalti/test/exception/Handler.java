package com.globalloyalti.test.exception;

import com.globalloyalti.test.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class Handler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = AlreadyExistException.class)
    public ResponseEntity<?> resp(AlreadyExistException e){
        log.info(e.getMessage());
        return new ResponseEntity<>(new CommonResponse<>(e.getResponseCode(), e.getResponseMessage()), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> resp(NotFoundException e){
        log.info(e.getMessage());
        return new ResponseEntity<>(new CommonResponse<>(e.getResponseCode(), e.getResponseMessage()), HttpStatus.OK);
    }
}
