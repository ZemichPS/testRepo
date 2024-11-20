package by.zemich.userms.controller;

import by.zemich.userms.controller.request.AuthLoginPasswordRequest;
import by.zemich.userms.controller.request.ChangePasswordRequest;
import by.zemich.userms.controller.request.RegisterRequest;
import by.zemich.userms.dao.entity.User;
import by.zemich.userms.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String login,
                                        @RequestParam String password
    ) {
        String jwt = authService.authenticate(login, password);
        return ResponseEntity.ok(jwt);
    }

    @PatchMapping
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request
    ) {
        String jwt = authService.changePassword(
                request.getLogin(),
                request.getOldPassword(),
                request.getNewPassword()
        );
        return ResponseEntity.ok(jwt);
    }


}
