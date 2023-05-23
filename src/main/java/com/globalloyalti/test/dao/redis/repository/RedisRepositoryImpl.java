package com.globalloyalti.test.dao.redis.repository;

import com.globalloyalti.test.dao.redis.entity.RedisEntity;
import com.globalloyalti.test.util.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    private final String KEY = "MY_KEY";

    private final ReactiveRedisComponent reactiveRedisComponent;

    @Override
    public Mono<RedisEntity> getOne(String id) {
        return reactiveRedisComponent.get(KEY, id).flatMap(d -> Mono.just(ObjectMapperUtils.objectMapper(d, RedisEntity.class)));
    }

    @Override
    public Mono<RedisEntity> save(RedisEntity entity) {
        return reactiveRedisComponent.set(KEY, entity.getId(), entity).map(b -> entity);

    }

    @Override
    public Mono<Void> update(RedisEntity entity) {
        return null;
    }

}
