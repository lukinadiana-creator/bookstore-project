package com.diana.bookstore.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public void createUser (@RequestParam String name,
                            @RequestParam String email,
                            @RequestParam String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(name, email, encodedPassword);
        userService.save(user);
    }

    @GetMapping("/me")
    public UserDto getUser(Authentication authentication) {
        return userService.getUser(authentication.getName());
    }
}
