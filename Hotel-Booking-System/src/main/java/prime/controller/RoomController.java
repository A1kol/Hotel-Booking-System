package prime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import prime.dto.RoomDTO;
import prime.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole(, 'ADMIN')")
    public ResponseEntity<Void> addRoom(@RequestBody RoomDTO dto) {
        roomService.addExecute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<List<RoomDTO>> viewAll() {
        return ResponseEntity.ok(roomService.viewAllExecute());
    }
}
