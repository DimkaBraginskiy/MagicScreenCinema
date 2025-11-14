package com.magicscreencinema.domain.persistence.testmodel;

import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ElementCollection(name = "person")
public class SimplePerson {
    @Id
    private UUID id;
    private String name;
    private int age;
    private List<DayOfWeek> days;
    private String email;

    public SimplePerson(UUID id, String name, int age, List<DayOfWeek> days, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.days = days;
        this.email = email;
    }

    SimplePerson() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public List<DayOfWeek> getDays() {
        return days;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }
}
