package com.globalloyalti.test.dao.h2db.repository;

import com.globalloyalti.test.dao.h2db.entity.Dog;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
public interface DogRepository extends R2dbcRepository<Dog, String> {


    @Transactional
    @Modifying
    @Query("INSERT INTO mydb.dogs (name, breed) VALUES (?1, ?2)")
    Mono<Dog> save(String name, String[] breed);

}
