package com.magicscreencinema.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationKey(UUID reservationNumber, LocalDateTime reservationTime) {
}
