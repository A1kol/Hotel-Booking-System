package prime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prime.entity.User;
import prime.enums.Role;
import prime.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void changeUserRole(Long id, Role role) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User was not found")
        );
                user.setRole(role);
                userRepository.save(user);
    }
}
