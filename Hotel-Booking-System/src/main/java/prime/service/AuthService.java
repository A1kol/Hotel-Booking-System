package prime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import prime.tools.Mapper;
import prime.config.JwtService;
import prime.dto.AuthRequestDTO;
import prime.dto.AuthResponseDTO;
import prime.entity.*;
import prime.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Mapper swagger;

    public void executeRegister(AuthRequestDTO request) {
        if(userRepository.existsByMail(request.mail())) {
            throw new RuntimeException("User already exists");
        }
        userRepository.save(swagger.toUser(request));
    }

    public AuthResponseDTO executeLogin(AuthRequestDTO request) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.mail(),
                            request.password())
            );

        User user = userRepository.findByMail(request.mail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(user.getMail(), token);
    }
}