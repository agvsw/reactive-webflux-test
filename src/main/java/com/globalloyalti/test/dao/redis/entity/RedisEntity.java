package com.globalloyalti.test.dao.redis.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@RedisHash
public class RedisEntity implements Serializable {


    @Serial
    private static final long serialVersionUID = 6638155646776320724L;

    private String id;
    private String date;

    @Override
    public String toString(){
        return String.format("{ id: %s, date: %s }", id, date);
    }
}
