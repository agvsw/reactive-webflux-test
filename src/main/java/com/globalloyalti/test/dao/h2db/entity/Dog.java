package com.globalloyalti.test.dao.h2db.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dogs", schema = "mydb")
@Accessors(chain = true)
public class Dog implements Persistable<String> {

    @Id
    private String name;
    private List<String> breed;


    @Transient
    private boolean newDog;

    @Override
    public String getId() {
        return name;
    }

    @Transient
    @Override
    public boolean isNew() {
        return this.newDog;
    }


    @Override
    public String toString(){
        return String.format("{name: %s, breed: %s}", name, String.join(",", breed));
    }

}
