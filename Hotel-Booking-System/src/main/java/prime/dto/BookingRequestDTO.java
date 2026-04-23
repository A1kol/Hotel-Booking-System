package prime.dto;

import java.time.LocalDate;

public record BookingRequestDTO(
        String roomNumber,
        LocalDate checkIn,
        LocalDate checkOut
) {
}
