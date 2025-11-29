package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.HallTypeEnum;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ElementCollection(name = "halls")
public class Hall {
    //--basic fields
    @Id
    private UUID id;
    private int hallNumber;
    private HallTypeEnum hallType;
    private int maxRow;
    private int rowWidth;

    //--associations
    @OneToMany(cascade = {Cascade.SAVE, Cascade.DELETE})
    private List<Seat> seats; //TODO change to set
    private Set<Seance> seances;

    //--constructors
    public Hall() {
        seances = new HashSet<>();
        //TODO add seats = new HashSet<>();
    }
    public Hall(int hallNumber, HallTypeEnum hallType, int maxRow, int rowWidth) {
        this();
        this.hallNumber = FieldValidator.validatePositiveNumber(hallNumber, "Hall Number");
        this.hallType = FieldValidator.validateObjectNotNull(hallType, "Hall Type");
        this.maxRow = FieldValidator.validatePositiveNumber(maxRow, "Max Row");
        this.rowWidth = FieldValidator.validatePositiveNumber(rowWidth, "Row Width");
    }
    public Hall(int hallNumber, HallTypeEnum hallType, int maxRow, int rowWidth, List<Seat> seats, Set<Seance> seances) {
        this(hallNumber, hallType, maxRow, rowWidth);
        this.seats = FieldValidator.validateSeatsInHallNotNull(seats, this);

        FieldValidator.validateObjectNotNull(seances, "seances");
        for (Seance seance : seances) {
            addSeance(seance);
        }


        //TODO dima
        //FieldValidator.validateObjectNotNull(seats, "seats");
        // for (Seat seat : seats) {
        //      addSeat(seat);
        // }
        //
    }

    //--association logic
    // seance
    void addSeance(Seance seance) {
        FieldValidator.validateObjectNotNull(seance, "Seance");
        this.seances.add(seance);
    }
    void removeSeance(Seance seance) {
        FieldValidator.validateObjectNotNull(seance, "Seance");
        this.seances.remove(seance);
    }

    //--setters
    public void setRowWidth(int rowWidth) {
        this.rowWidth = FieldValidator.validatePositiveNumber(rowWidth, "Row Width");
    }
    public void setSeats(List<Seat> seats) {
        this.seats = FieldValidator.validateSeatsInHallNotNull(seats, this);
    }
    public void setHallNumber(int hallNumber) {
        this.hallNumber = FieldValidator.validatePositiveNumber(hallNumber, "Hall Number");
    }
    public void setHallType(HallTypeEnum hallType) {
        this.hallType = FieldValidator.validateObjectNotNull(hallType, "Hall Type");
    }
    public void setMaxRow(int maxRow) {
        this.maxRow = FieldValidator.validatePositiveNumber(maxRow, "Max Row");
    }
    public void setId(UUID id) {
        this.id = id;
    }

    //--getters
    public Set<Seance> getSeances() {
        return new HashSet<>(seances);
    }
    public UUID getId() {
        return id;
    }
    public int getMaxRow() {
        return maxRow;
    }
    public HallTypeEnum getHallType() {
        return hallType;
    }
    public List<Seat> getSeats() {
        return seats;
    }
    public int getRowWidth() {
        return rowWidth;
    }
    public int getHallNumber() {
        return hallNumber;
    }
}