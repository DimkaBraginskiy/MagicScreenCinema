package com.magicscreencinema.domain.persistence.testmodel;

import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@ElementCollection(name = "person")
public class PersonWithNoArgsConstructor {
    @Id
    private UUID id;
    private String name;
    private int age;
    private List<DayOfWeek> days;
    private String email;

    public PersonWithNoArgsConstructor(UUID id, String name, int age, List<DayOfWeek> days, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.days = days;
        this.email = email;
    }
}
