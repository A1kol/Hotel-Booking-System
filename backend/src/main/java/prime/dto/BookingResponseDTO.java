package prime.dto;

import prime.enums.Status;

import java.time.LocalDate;

public record BookingResponseDTO(
        Long id,
        String roomNumber,
        LocalDate checkIn,
        LocalDate checkOut,
        Status status
) {
}
