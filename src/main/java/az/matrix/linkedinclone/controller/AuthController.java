package az.matrix.linkedinclone.controller;

import az.matrix.linkedinclone.dto.request.AuthRequest;
import az.matrix.linkedinclone.dto.request.UserRequest;
import az.matrix.linkedinclone.dto.response.AuthResponse;
import az.matrix.linkedinclone.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/sign-in")
    public AuthResponse register(@Validated @RequestBody UserRequest userRequest){
        return authService.register(userRequest);
    }

    @PostMapping("/signup")
    public AuthResponse login(@Validated @RequestBody AuthRequest authRequest){
        return authService.login(authRequest);
    }

}
