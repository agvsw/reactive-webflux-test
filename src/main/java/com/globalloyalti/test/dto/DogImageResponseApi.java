package com.globalloyalti.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DogImageResponseApi {
    private List<String> message;
    private String status;
}
