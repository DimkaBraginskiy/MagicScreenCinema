package com.magicscreencinema.domain.model;

import com.magicscreencinema.persistence.QualifierKeyConverter;

import java.util.UUID;

public class ReservationKeyConverter implements QualifierKeyConverter<ReservationKey> {
    private static final String SEPARATOR = "|";

    @Override
    public String encode(ReservationKey key) {
        return key.reservationNumber() + SEPARATOR + key.reservationTime();
    }

    @Override
    public ReservationKey decode(String key) {
        String[] parts = key.split("\\" + SEPARATOR);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid encoded ReservationKey: " + key);
        }
        return new ReservationKey(
                UUID.fromString(parts[0]),
                java.time.LocalDateTime.parse(parts[1])
        );
    }

}
