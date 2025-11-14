package com.magicscreencinema.domain.persistence.testmodel;

import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.util.UUID;

@ElementCollection(name = "person")
public class PersonAnnotatedWithId {
    @Id
    private UUID id;
}
