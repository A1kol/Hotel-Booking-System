package prime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import prime.dto.BookingRequestDTO;
import prime.dto.BookingResponseDTO;
import prime.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;


    @PostMapping("/")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Void> apply(@RequestBody BookingRequestDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        String mail = userDetails.getUsername();

        bookingService.createBooking(dto, mail);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<List<BookingResponseDTO>> viewAll() {
        return ResponseEntity.ok(bookingService.findAll());
    }

    @PatchMapping("/approve/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        bookingService.approveBooking(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
