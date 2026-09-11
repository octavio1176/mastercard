package mastercard.System.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mastercard.System.domain.enums.UserRoles;
import mastercard.System.dto.LoginRequestDTO;
import mastercard.System.domain.entity.User;
import mastercard.System.dto.RegisterRequestDTO;
import mastercard.System.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenService tokenService;

    public ResponseEntity<String> login(LoginRequestDTO request) {

        var authToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var auth = authenticationManager.authenticate(authToken);

        User user = (User) auth.getPrincipal();

        String token = tokenService.generate(user);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    public ResponseEntity<String> register(RegisterRequestDTO request) {

        if (userRepository.findByUsername(request.username()).isPresent()) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }

        String password = passwordEncoder.encode(request.password());

        User userToBeSaved = User.builder()
                .roles(UserRoles.ADMIN)
                .username(request.username())
                .email(request.email())
                .password(password)
                .build();

        User saved = userRepository.save(userToBeSaved);
        log.info("Usuario criado || id:{}, username:{}, email:{}", saved.getId(), saved.getUsername(), saved.getEmail());

        return new ResponseEntity<>("User CREATED SUCCESSFULLY", HttpStatus.CREATED);
    }
}
