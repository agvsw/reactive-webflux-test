package com.globalloyalti.test.service.implement;

import com.globalloyalti.test.dao.h2db.entity.Dog;
import com.globalloyalti.test.dao.h2db.repository.DogRepository;
import com.globalloyalti.test.dao.redis.entity.RedisEntity;
import com.globalloyalti.test.dao.redis.repository.RedisRepository;
import com.globalloyalti.test.dto.*;
import com.globalloyalti.test.exception.AlreadyExistException;
import com.globalloyalti.test.exception.NotFoundException;
import com.globalloyalti.test.service.DogService;
import com.globalloyalti.test.util.DogDtoMapper;
import com.globalloyalti.test.util.ResponseApiMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DogServiceImpl implements DogService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private DogRepository dogRepository;

    @Autowired
    private RedisRepository redisRepository;

    @Override
    public Mono<CommonResponse<DogResponseDto>> getListDog() {
        return checkLastDataUpdated()
                .then(mappingBreed());
    }


    private Mono<Void> checkLastDataUpdated() {
        return redisRepository.getOne("fetch_time")
                .flatMap(redis -> {
                    log.info("data redis " + redis.toString());

                    if (shouldUpdateData(redis)) {
                        log.info("data expired");
                        return updateFromApi("satu");
                    } else {
                        return Mono.just("no need updated");
                    }
                })
                .switchIfEmpty(Mono.defer(() -> updateFromApi("dua")))
                .then();
    }


    private boolean shouldUpdateData(RedisEntity redis) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(redis.getDate());
            long minutesElapsed = (new Date().getTime() - date.getTime()) / (60 * 1000) % 60;
            log.info("minute " +minutesElapsed);
            return minutesElapsed > 10;
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }

//    private Mono<Void> updateFromApi() {
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder.path("/api/breeds/list/all").build())
//                .retrieve()
//                .bodyToMono(DogResponseApi.class)
//                .flatMap(responseApi -> {
//                    log.info("success get from api");
//                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String dateNow = dateFormat.format(new Date());
//                    return redisRepository.update(new RedisEntity("fetch_time", dateNow))
//                            .thenMany(dogRepository.saveAll(ResponseApiMapper.mapToDogModel(responseApi)))
//                            .then();
//                });
//    }

    private Mono<Void> updateFromApi(String params) {

        log.info("update from " + params);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/breeds/list/all").build())
                .retrieve()
                .bodyToMono(DogResponseApi.class)
                .flatMap(responseApi -> {
                    log.info("success get from api 1");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateNow = dateFormat.format(new Date());
                    return redisRepository.save(new RedisEntity("fetch_time", dateNow))
                            .thenMany(Flux.fromIterable(ResponseApiMapper.mapToDogModel(responseApi)))
                            .flatMap(this::saveOrUpdate)
                            .then();
                });
    }

    private Mono<Dog> saveOrUpdate(Dog dog) {
        return dogRepository.findById(dog.getName())
                .flatMap(existingDog -> {
                    existingDog.setName(dog.getName());
                    existingDog.setBreed(dog.getBreed());
                    return dogRepository.save(existingDog);
                })
                .switchIfEmpty(dogRepository.save(dog.getName(), String.join(",", dog.getBreed()).split(",")));
    }


    @Override
    public Mono<CommonResponse<DogDto>> save(DogDto dogDto) {
        return checkLastDataUpdated()
                .then(checkIfExist(dogDto.getName())
                        .flatMap(exists -> {
                            log.info("exists " + exists);
                            if (exists) {
                                return Mono.error(new AlreadyExistException(44, String.format("name %s already exists", dogDto.getName())));
                            } else {
                                return dogRepository.save(DogDtoMapper.mapToDogModel(dogDto))
                                        .map(savedDog -> {
                                            CommonResponse<DogDto> response = new CommonResponse<>();
                                            response.setData(DogDtoMapper.mapToDogDto(savedDog));
                                            return response;
                                        });
                            }
                        })
                );
    }



    @Override
    public Mono<CommonResponse<DogDto>> update(DogDto dogDto) {
        return dogRepository.save(DogDtoMapper.mapToDogModel(dogDto)).flatMap(dog -> {
            CommonResponse<DogDto> response = new CommonResponse<>();
            response.setData(DogDtoMapper.mapToDogDto(dog));
            return Mono.just(response);
        });
    }

    @Override
    public Mono<CommonResponse<DogDto>> getById(String id) {
        return checkLastDataUpdated()
                .then(dogRepository.findById(id))
                .flatMap(dog -> {
                    CommonResponse<DogDto> response = new CommonResponse<>();
                    response.setData(DogDtoMapper.mapToDogDto(dog));
                    return Mono.just(response);
                })
                .switchIfEmpty(Mono.error(new NotFoundException(404, String.format("Dog %s not found", id))));
    }



    @Override
    public Mono<Void> delete(String id) {
        return dogRepository.deleteById(id);
    }


    private Mono<Boolean> checkIfExist(String id) {
        return dogRepository.findById(id)
                .hasElement()
                .map(exists -> exists);
    }


    private Mono<CommonResponse<DogResponseDto>> mappingBreed() {
        return dogRepository
                .findAll()
                .collectList()
                .flatMap(dogs -> {
                    if (dogs.isEmpty()) {
                        return Mono.error(new NotFoundException(20, "No data"));
                    }

                    Map<String, Object> objectMap = new HashMap<>();
                    Flux<Mono<Void>> mappingOperations = Flux.fromIterable(dogs)
                            .flatMap(dog -> {
                                if (dog.getName().equals("sheepdog") || dog.getName().equals("terrier") || dog.getName().equals("shiba")) {
                                    if (dog.getName().equals("sheepdog")) {
                                        dog.getBreed().forEach(breed -> objectMap.put(String.format("%s-%s", dog.getName(), breed), new ArrayList<>()));
                                        return Mono.empty();
                                    } else if (dog.getName().equals("shiba")) {
                                        return mappingShibaImage(dog.getName(), objectMap).then(Mono.empty());
                                    } else {
                                        return mappingImage(dog.getName(), dog.getBreed(), objectMap).then(Mono.empty());
                                    }
                                } else {
                                    objectMap.put(dog.getName(), dog.getBreed());
                                    return Mono.empty();
                                }
                            });
                    return Mono.when(mappingOperations).thenReturn(createResponse(objectMap));
                });
    }

    private Mono<Void> mappingImage(String name, List<String> listBreed, Map<String, Object> objectMap) {
        return getImageList(name)
                .flatMap(responseApi -> {
                    for (String breed : listBreed) {
                        List<String> filteredImages = responseApi.getMessage().stream()
                                .filter(img -> img.contains(String.format("%s-%s", name, breed)))
                                .collect(Collectors.toList());
                        objectMap.put(String.format("%s-%s", name, breed), filteredImages);
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> mappingShibaImage(String name, Map<String, Object> objectMap) {
        return getImageList(name)
                .flatMap(responseApi -> {
                    List<String> imageList = responseApi.getMessage().stream()
                            .filter(img -> (Integer.parseInt(img.replaceAll("[^0-9]", "")) % 2) != 0)
                            .collect(Collectors.toList());

                    objectMap.put(name, imageList);
                    return Mono.empty();
                });
    }

    private CommonResponse<DogResponseDto> createResponse(Map<String, Object> objectMap) {
        DogResponseDto dto = new DogResponseDto();
        dto.setBreed(objectMap);
        CommonResponse<DogResponseDto> response = new CommonResponse<>();
        response.setData(dto);
        return response;
    }

    private Mono<DogImageResponseApi> getImageList(String name) {
        return webClient.get()
                .uri(String.format("/api/breed/%s/images", name))
                .retrieve()
                .bodyToMono(DogImageResponseApi.class);
    }

}
