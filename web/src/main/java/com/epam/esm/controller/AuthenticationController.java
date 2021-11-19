package com.epam.esm.controller;


import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserDtoMapper;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.model.AuthToken;
import com.epam.esm.model.User;
import com.epam.esm.security.TokenProvider;
import com.epam.esm.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {


    private final AuthenticationManager authenticationManager;
    private final TokenProvider jwtTokenUtil;
    private final UserService service;
    private final UserDtoMapper userDtoMapper;
    private final BCryptPasswordEncoder bcryptEncoder;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenProvider jwtTokenUtil,
                                    UserService service, UserDtoMapper userDtoMapper, BCryptPasswordEncoder bcryptEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.service = service;
        this.userDtoMapper = userDtoMapper;
        this.bcryptEncoder = bcryptEncoder;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody User user) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new AuthToken(token));
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public void signUp(@RequestBody User user) {
        try {
            service.getUserByUsername(user.getUsername());
            throw new InvalidRequestException("User " + user.getUsername() + " already exists");
        } catch (UserNotFoundException e) {
            user.setRole("ADMIN");
            user.setPassword(bcryptEncoder.encode(user.getPassword()));
            service.create(user);
        }
    }

}