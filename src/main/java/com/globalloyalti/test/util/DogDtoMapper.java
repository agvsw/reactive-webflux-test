package com.globalloyalti.test.util;

import com.globalloyalti.test.dao.h2db.entity.Dog;
import com.globalloyalti.test.dto.DogDto;

public class DogDtoMapper {

    public static DogDto mapToDogDto(Dog dog){
        return new DogDto(
                dog.getName(),
                dog.getBreed()
        );
    }

    public static Dog mapToDogModel(DogDto dog){
        return new Dog(
                dog.getName(),
                dog.getBreed(),
                true
        );
    }

}
