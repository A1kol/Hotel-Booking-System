package prime.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import prime.dto.AuthRequestDTO;
import prime.dto.BookingRequestDTO;
import prime.dto.BookingResponseDTO;
import prime.dto.RoomDTO;
import prime.entity.Booking;
import prime.entity.Room;
import prime.entity.User;
import prime.enums.Role;
import prime.enums.Status;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final PasswordEncoder passwordEncoder;

    public User toUser(AuthRequestDTO dto) {
        return User.builder()
                .mail(dto.mail())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.USER)
                .build();
    }

    public Room toRoom(RoomDTO dto) {
        return Room.builder()
                .roomNumber(dto.roomNumber())
                .type(dto.type())
                .pricePerNight(dto.pricePerNight())
                .capacity(dto.capacity())
                .build();
    }

    public static RoomDTO toRoomDTO(Room entity) {
        return new RoomDTO(
                entity.getRoomNumber(),
                entity.getType(),
                entity.getPricePerNight(),
                entity.getCapacity()
        );
    }

    public static Booking toBooking(BookingRequestDTO dto, User user, Room room, Integer totalPrice) {

        return Booking.builder()
                .user(user)
                .room(room)
                .checkInDate(dto.checkIn())
                .checkOutDate(dto.checkOut())
                .totalPrice(totalPrice)
                .status(Status.PENDING)
                .build();
    }

    public static BookingResponseDTO toBookingDTO(Booking entity) {
        return new BookingResponseDTO(
                entity.getId(),
                entity.getRoom().getRoomNumber(),
                entity.getCheckInDate(),
                entity.getCheckOutDate(),
                entity.getStatus()
        );
    }
}
