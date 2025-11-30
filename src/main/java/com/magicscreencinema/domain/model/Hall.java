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
    private Set<Seat> seats;
    private Set<Seance> seances;

    //--constructors
    private Hall() {}
    public Hall(int hallNumber, HallTypeEnum hallType, int maxRow, int rowWidth) {
        this.hallNumber = FieldValidator.validatePositiveNumber(hallNumber, "Hall Number");
        this.hallType = FieldValidator.validateObjectNotNull(hallType, "Hall Type");
        this.maxRow = FieldValidator.validatePositiveNumber(maxRow, "Max Row");
        this.rowWidth = FieldValidator.validatePositiveNumber(rowWidth, "Row Width");

        seances = new HashSet<>();
        seats = new HashSet<>();
    }
    public Hall(int hallNumber, HallTypeEnum hallType, int maxRow, int rowWidth, Set<Seance> seances) {
        this(hallNumber, hallType, maxRow, rowWidth);

        FieldValidator.validateObjectNotNull(seances, "seances");
        for (Seance seance : seances) {
            addSeance(seance);
        }
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

    //--association logic
    // seat
    public Seat addSeat(int seatNumber, int row){
        Seat newSeat = new Seat(seatNumber, row, this);
        FieldValidator.validateSeatDimension(newSeat, this);
        FieldValidator.validateSeatNotDuplicate(newSeat, this);

        this.seats.add(newSeat);
        return newSeat;
    }
    public void removeSeat(Seat seat){
        FieldValidator.validateObjectNotNull(seat, "seat");

        if(!this.seats.contains(seat)) return;

        if(this.seats.size() == 1) return;

        this.seats.remove(seat);

    }

    //--setters
    public void setRowWidth(int rowWidth) {
        this.rowWidth = FieldValidator.validatePositiveNumber(rowWidth, "Row Width");
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
    public Set<Seat> getSeats() {
        return new HashSet<>(seats);
    }
    public int getRowWidth() {
        return rowWidth;
    }
    public int getHallNumber() {
        return hallNumber;
    }
}