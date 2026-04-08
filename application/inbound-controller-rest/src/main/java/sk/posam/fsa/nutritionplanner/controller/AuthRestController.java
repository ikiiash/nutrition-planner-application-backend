package sk.posam.fsa.nutritionplanner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import sk.posam.fsa.nutritionplanner.domain.auth.service.AuthService;
import sk.posam.fsa.nutritionplanner.rest.api.AuthApi;
import sk.posam.fsa.nutritionplanner.rest.dto.LoginRequestDto;
import sk.posam.fsa.nutritionplanner.rest.dto.LoginResponseDto;
import sk.posam.fsa.nutritionplanner.security.JwtService;

@RestController
public class AuthRestController implements AuthApi {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthRestController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<LoginResponseDto> loginUser(LoginRequestDto loginRequestDto) {
        String role = authService.login(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword()
        );

        if (role == null) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtService.generateToken(loginRequestDto.getEmail(), role);

        LoginResponseDto response = new LoginResponseDto();
        response.setAccessToken(token);
        response.setTokenType("Bearer");
        response.setExpiresIn(3600L);

        return ResponseEntity.ok(response);
    }
}