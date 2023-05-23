package com.globalloyalti.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResponse<T> {
    private int responseCode;
    private String responseMessage;
    private T data;

    public CommonResponse(){
        this.responseCode= 20;
        this.responseMessage= "success";
    }
    public CommonResponse(int responseCode, String responseMessage){
        this.responseCode=responseCode;
        this.responseMessage=responseMessage;
    }

    public CommonResponse(T data){
        this.responseCode= 20;
        this.responseMessage= "success";
        this.data = data;
    }
}
