package mastercard.System.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mastercard.System.dto.LoginRequestDTO;
import mastercard.System.dto.RegisterRequestDTO;
import mastercard.System.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequestDTO request) {
        return authService.register(request);
    }

}
