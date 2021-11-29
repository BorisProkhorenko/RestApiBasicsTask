package com.epam.esm.controller;


import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {


    private final UserService service;

    private final BCryptPasswordEncoder bcryptEncoder;

    public AuthenticationController(UserService service, BCryptPasswordEncoder bcryptEncoder) {
        this.service = service;
        this.bcryptEncoder = bcryptEncoder;
    }


    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public void signUp(@RequestBody User user) {
        try {
            service.findUserByUsername(user.getUsername());
            throw new InvalidRequestException("User " + user.getUsername() + " already exists");
        } catch (UserNotFoundException e) {
            user.setRole(User.Role.USER);
            user.setPassword(bcryptEncoder.encode(user.getPassword()));
            service.create(user);
        }
    }

}