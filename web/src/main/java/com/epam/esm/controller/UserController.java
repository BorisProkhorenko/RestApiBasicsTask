package com.epam.esm.controller;


import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
        * This Controller provides public API for operations with {@link User} entity.
        * Uses {@link UserService} to access Data Base through business-logic layer.
        *
        * @author Boris Prokhorenko
        * @see User
        * @see UserService
        */
@RestController
@RequestMapping(value = "/users")
public class UserController {

            private final UserService service;

            public UserController(UserService service) {
                this.service = service;
            }

    /**
     * Method allows getting {@link User} from DB by its id
     *
     * @param id - primary key to search {@link User} entity object in DB
     * @return {@link User} entity object from DB
     */
    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    /**
     * Method allows getting all {@link User} entity objects from DB
     *
     * @return {@link List} of {@link User} entity objects from DB
     */
    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }
        }
