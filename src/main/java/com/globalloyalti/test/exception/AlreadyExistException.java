package com.globalloyalti.test.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public class AlreadyExistException extends Exception {

    @Serial
    private static final long serialVersionUID = 3439283527881975188L;

    private int responseCode;
    private String responseMessage;

    public AlreadyExistException(int code, String message){
        super();
        this.responseCode = code;
        this.responseMessage = message;
    }
}
