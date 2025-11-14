package com.magicscreencinema.domain.persistence.testmodel;

import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.time.DayOfWeek;
import java.util.List;

@ElementCollection(name = "person")
public class SimplePersonWithStringID {
    @Id
    private String id;
    private String name;
    private int age;
    private List<DayOfWeek> days;
    private String email;

    SimplePersonWithStringID() {
    }
}
