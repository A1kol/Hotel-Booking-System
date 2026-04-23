package prime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prime.dto.BookingRequestDTO;
import prime.dto.BookingResponseDTO;
import prime.entity.Booking;
import prime.entity.Room;
import prime.entity.User;
import prime.enums.Status;
import prime.repository.BookingRepository;
import prime.repository.RoomRepository;
import prime.repository.UserRepository;
import prime.tools.Mapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;


    public void createBooking(BookingRequestDTO dto, String mail) {
        User user = userRepository.findByMail(mail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepository.findByRoomNumber(dto.roomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if(bookingRepository.existsOverlappingBookings(
                dto.roomNumber(),
                dto.checkIn(),
                dto.checkOut())) {
            throw new RuntimeException("The room is already in use");
        }


        long nights = java.time.temporal.ChronoUnit.DAYS.between(dto.checkIn(), dto.checkOut());
        Integer totalPrice = (int) (nights * room.getPricePerNight());

        bookingRepository.save(Mapper.toBooking(dto, user, room, totalPrice));
    }

    public List<BookingResponseDTO> findAll() {
        return bookingRepository.findAll()
                .stream()
                .map(Mapper::toBookingDTO)
                .toList();
    }

    public void approveBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking was not found"));

        booking.setStatus(Status.CONFIRMED);

        bookingRepository.save(booking);
        System.out.println(booking.getStatus());
    }
}
