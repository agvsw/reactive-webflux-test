package com.globalloyalti.test;

import com.globalloyalti.test.controller.DogController;
import com.globalloyalti.test.dto.CommonResponse;
import com.globalloyalti.test.dto.DogResponseDto;
import com.globalloyalti.test.service.DogService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient
public class DogControllerTest {

    @Mock
    private DogService dogService;

    @InjectMocks
    private DogController dogController;

    @Autowired
    private WebTestClient webTestClient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        webTestClient = WebTestClient.bindToController(dogController).build();
    }


    @Test
    public void testGetAllDog() throws Exception {
        DogResponseDto dogResponseDto = new DogResponseDto();
        when(dogService.getListDog()).thenReturn(Mono.just(new CommonResponse<>(200, "Success", dogResponseDto)));

        webTestClient.get()
                .uri("/api/v1/breeds/list")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommonResponse.class)
                .value(response -> {
                    assertEquals(20, response.getResponseCode());
                    assertEquals("success", response.getResponseMessage());
                    assertNotNull(response.getData());
                });
    }



}
