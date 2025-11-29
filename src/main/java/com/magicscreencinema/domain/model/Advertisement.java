package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.ElementCollection;
import com.magicscreencinema.persistence.declaration.Id;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ElementCollection(name = "advertisements")
public class Advertisement {
    //--basic fields
    @Id
    private UUID id;
    private String name;
    private long duration;
    private String advertiserName;

    //--associations
    private Set<Seance> seances;

    //--constructors
    private Advertisement() {
        this.seances = new HashSet<>();
    }
    public Advertisement(String name, long duration, String advertiserName) {
        this();
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
        this.duration = FieldValidator.validatePositiveNumber(duration, "Duration");
        this.advertiserName = FieldValidator.validateNullOrEmptyString(advertiserName, "Advertiser Name");
        id = UUID.randomUUID();
    }
    public Advertisement(String name, long duration, String advertiserName, Set<Seance> seances){
        this(name, duration, advertiserName);

        FieldValidator.validateObjectNotNull(seances, "seances");
        for (Seance seance : seances){
            addSeance(seance);
        }
    }

    //--association logic
    //seance
    void addSeance(Seance seance) {
        FieldValidator.validateObjectNotNull(seance, "Seance");
        this.seances.add(seance);
    }
    void removeSeance(Seance seance) {
        FieldValidator.validateObjectNotNull(seance, "Seance");
        this.seances.remove(seance);
    }

    //--getters
    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public long getDuration() {
        return duration;
    }
    public String getAdvertiserName() {
        return advertiserName;
    }
    public Set<Seance> getSeances() {
        return new HashSet<>(seances);
    }

    //--setters
    public void setDuration(long duration) {
        this.duration = FieldValidator.validatePositiveNumber(duration, "Duration");
    }
    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = FieldValidator.validateNullOrEmptyString(advertiserName, "Advertiser Name");
    }
    public void setName(String name) {
        this.name = FieldValidator.validateNullOrEmptyString(name, "Name");
    }
}
