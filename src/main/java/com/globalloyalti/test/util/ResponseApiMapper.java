package com.globalloyalti.test.util;

import com.globalloyalti.test.dao.h2db.entity.Dog;
import com.globalloyalti.test.dto.DogResponseApi;

import java.util.*;

public class ResponseApiMapper {

    public static List<Dog> mapToDogModel(DogResponseApi responseApi){
        List<Dog> dogs = new ArrayList<>();
        for (Map.Entry<String,String[]> entry : responseApi.getMessage().entrySet()){
            Dog dog = new Dog();
            dog.setName(entry.getKey());
            dog.setBreed(Arrays.asList(responseApi.getMessage().get(entry.getKey())));
            dogs.add(dog);
        }
        return dogs;
    }
}
