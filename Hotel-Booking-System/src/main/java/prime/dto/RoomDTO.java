package prime.dto;

import prime.enums.Type;

public record RoomDTO(
        String roomNumber,
        Type type,
        Integer pricePerNight,
        Integer capacity
) {
}
