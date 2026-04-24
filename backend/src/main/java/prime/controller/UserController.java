package prime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import prime.enums.Role;
import prime.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Void> changeRole(@PathVariable Long id,@RequestBody Role role) {
        userService.changeUserRole(id, role);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
