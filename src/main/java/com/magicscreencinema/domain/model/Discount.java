package com.magicscreencinema.domain.model;

import com.magicscreencinema.domain.enums.AgeGroupEnum;
import com.magicscreencinema.domain.exception.InvalidDiscountException;
import com.magicscreencinema.domain.validation.FieldValidator;
import com.magicscreencinema.persistence.declaration.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ElementCollection(name = "discounts")
public abstract class Discount {
    @Id
    private UUID id;
    private double discountAmount;
    private String promoCode;
    @OneToMany(fetch = Fetch.EAGER)
    private Set<Reservation> reservations;
    @OneToOne(cascade = {Cascade.DELETE, Cascade.SAVE})
    private AgeGroupDiscount ageGroupDiscount;
    @OneToOne(cascade = {Cascade.DELETE, Cascade.SAVE})
    private SpecialConditionDiscount specialConditionDiscount;

    private Discount(double discountAmount, String promoCode) {
        if (discountAmount > 0 && discountAmount < 1) {
            this.discountAmount = discountAmount;
        } else {
            throw new InvalidDiscountException("Discount Amount must be a decimal value between 0 and 1");
        }

        this.promoCode = FieldValidator.validateNullOrEmptyString(promoCode, "Promo Code");
        id = UUID.randomUUID();
        reservations = new HashSet<>();
    }

    public Discount(double discountAmount, String promoCode, String ageGroupDiscountDescription, AgeGroupEnum ageGroup){
        this(discountAmount, promoCode);
        this.ageGroupDiscount = new AgeGroupDiscount(ageGroupDiscountDescription, ageGroup);
    }

    public Discount(double discountAmount, String promoCode, String specialConditionDescription){
        this(discountAmount, promoCode);
        this.specialConditionDiscount = new SpecialConditionDiscount(specialConditionDescription);
    }

    Discount() {
    }

    void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void setDiscountAmount(double discountAmount) {
        if (discountAmount > 0 && discountAmount < 1) {
            this.discountAmount = discountAmount;
        } else {
            throw new InvalidDiscountException("Discount Amount must be a decimal value between 0 and 1");
        }
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = FieldValidator.validateNullOrEmptyString(promoCode, "Promo Code");
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public UUID getId() {
        return id;
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }
    public Set<Reservation> getReservations(){
        return Set.copyOf(reservations);
    }

    public Optional<IAgeGroupDiscount> getAgeGroupDiscount() {
        return Optional.ofNullable(ageGroupDiscount);
    }

    public Optional<ISpecialConditionDiscount> getSpecialConditionDiscount(){
        return Optional.ofNullable(specialConditionDiscount);
    }


    @ElementCollection(name = "ageGroupDiscounts")
    private static class AgeGroupDiscount implements IAgeGroupDiscount{
        @Id
        private UUID id;
        private String description;
        private AgeGroupEnum group;

        public AgeGroupDiscount(String description, AgeGroupEnum group) {
            this.description = FieldValidator.validateNullOrEmptyString(description, "Description");
            this.group = FieldValidator.validateObjectNotNull(group, "Group");
            id = UUID.randomUUID();
        }

        private AgeGroupDiscount() {
        }

        @Override
        public void setDescription(String description) {
            this.description = FieldValidator.validateNullOrEmptyString(description, "Description");
        }
        @Override
        public void setGroup(AgeGroupEnum group) {
            this.group = FieldValidator.validateObjectNotNull(group, "Group");
        }
        @Override
        public String getDescription() {
            return description;
        }
        @Override
        public AgeGroupEnum getGroup() {
            return group;
        }
        @Override
        public UUID getId() {
            return id;
        }
    }


    @ElementCollection(name = "specialConditionDiscounts")
    private static class SpecialConditionDiscount implements ISpecialConditionDiscount{
        @Id
        private UUID id;
        private String conditionDescription;

        public SpecialConditionDiscount(String conditionDescription) {
            this.conditionDescription = FieldValidator.validateNullOrEmptyString(conditionDescription, "Condition Description");
            id = UUID.randomUUID();
        }

        private SpecialConditionDiscount() {
        }

        @Override
        public void setConditionDescription(String conditionDescription) {
            this.conditionDescription = FieldValidator.validateNullOrEmptyString(conditionDescription, "Condition Description");
        }
        @Override
        public String getConditionDescription() {
            return conditionDescription;
        }
        @Override
        public UUID getId() {
            return id;
        }
    }
}
