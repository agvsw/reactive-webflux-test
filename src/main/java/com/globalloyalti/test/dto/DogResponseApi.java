package com.globalloyalti.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DogResponseApi {
    private Map<String, String[]> message;
    private String status;
}
