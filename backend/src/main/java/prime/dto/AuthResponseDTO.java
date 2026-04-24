package prime.dto;

import prime.enums.Role;

public record AuthResponseDTO(
        String mail,
        String token,
        Role role,
        Long id
) {
}
