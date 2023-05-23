package com.globalloyalti.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DogDto {
    private String name;
    private List<String> breed;

}
