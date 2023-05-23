package com.globalloyalti.test.dao.redis.repository;


import com.globalloyalti.test.dao.redis.entity.RedisEntity;
import reactor.core.publisher.Mono;

public interface RedisRepository {
    Mono<RedisEntity> getOne(String id);
    Mono<RedisEntity> save(RedisEntity entity);
    Mono<Void> update(RedisEntity entity);
}
