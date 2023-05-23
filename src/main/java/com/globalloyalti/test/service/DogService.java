package com.globalloyalti.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.globalloyalti.test.dto.CommonResponse;
import com.globalloyalti.test.dto.DogDto;
import com.globalloyalti.test.dto.DogResponseDto;
import com.globalloyalti.test.dao.h2db.entity.Dog;
import reactor.core.publisher.Mono;

import java.text.ParseException;

public interface DogService {
    Mono<CommonResponse<DogResponseDto>> getListDog() throws Exception;
    Mono<CommonResponse<DogDto>> save(DogDto dogDto) throws Exception;
    Mono<CommonResponse<DogDto>> update(DogDto dogDto) throws Exception;
    Mono<CommonResponse<DogDto>> getById(String id) throws Exception;
    Mono<Void> delete(String id) throws Exception;
}
