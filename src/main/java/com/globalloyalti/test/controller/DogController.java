package com.globalloyalti.test.controller;

import com.globalloyalti.test.dto.CommonResponse;
import com.globalloyalti.test.dto.DogDto;
import com.globalloyalti.test.dto.DogResponseDto;
import com.globalloyalti.test.service.DogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@Api(tags = "Dog API")
public class DogController {

    @Autowired
    private DogService dogService;

    @GetMapping("/breeds/list")
    @Transactional(timeout = 5)
    @ApiOperation("Get all dogs")
    public Mono<CommonResponse<DogResponseDto>> getAllDog() throws Exception {
        return dogService.getListDog();
    }

    @GetMapping("/breed/{id}/list")
    @Transactional(timeout = 2)
    @ApiOperation("Get dog by ID")
    public Mono<CommonResponse<DogDto>> getByName(@PathVariable("id") String id) throws Exception {
        return dogService.getById(id);
    }

    @PostMapping("/breed")
    @Transactional(timeout = 5)
    @ApiOperation("Save a dog")
    public Mono<CommonResponse<DogDto>> save(@RequestBody DogDto dogDto) throws Exception {
        return dogService.save(dogDto);
    }

    @PutMapping("/breed")
    @Transactional(timeout = 5)
    @ApiOperation("Update a dog")
    public Mono<CommonResponse<DogDto>> update(@RequestBody DogDto dogDto) throws Exception {
        return dogService.update(dogDto);
    }

    @DeleteMapping("/breed/{id}")
    @Transactional(timeout = 5)
    @ApiOperation("Delete a dog")
    public Mono<Void> delete(@PathVariable("id") String id) throws Exception {
        return dogService.delete(id);
    }

}
