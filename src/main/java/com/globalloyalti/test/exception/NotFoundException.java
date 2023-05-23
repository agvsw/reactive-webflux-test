package com.globalloyalti.test.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serial;

@Getter
@Setter
public class NotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 7333462999297928302L;

    private int responseCode;
    private String responseMessage;

    public NotFoundException(int code, String message){
        super();
        this.responseCode = code;
        this.responseMessage = message;
    }
}
