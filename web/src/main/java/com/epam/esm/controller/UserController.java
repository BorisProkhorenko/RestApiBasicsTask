package com.epam.esm.controller;


import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderDtoMapper;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserDtoMapper;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This Controller provides public API for operations with {@link User} and {@link Order} entity.
 * Uses {@link UserService} to access Data Base through business-logic layer.
 *
 * @author Boris Prokhorenko
 * @see User
 * @see Order
 * @see UserService
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService service;
    private final ObjectMapper objectMapper;
    private final UserDtoMapper userDtoMapper;
    private final OrderDtoMapper orderDtoMapper;

    public UserController(UserService service, ObjectMapper objectMapper, UserDtoMapper userDtoMapper,
                          OrderDtoMapper orderDtoMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.userDtoMapper = userDtoMapper;
        this.orderDtoMapper = orderDtoMapper;
    }

    /**
     * Method allows getting {@link User} from DB by its id
     *
     * @param id - primary key to search {@link User} entity object in DB
     * @return {@link User} entity object from DB
     */
    @GetMapping(value = "/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userDtoMapper.toDto(service.getUserById(id));
    }

    /**
     * Method allows getting all {@link User} entity objects from DB
     *
     * @return {@link List} of {@link User} entity objects from DB without orders.
     */
    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(name = "limit") Optional<Integer> limit,
                                     @RequestParam(name = "offset") Optional<Integer> offset) {
        return service.getAllUsers(limit,offset)
                .stream()
                .peek(user -> user.setOrders(new HashSet<>()))
                .map(userDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Method allows getting {@link Order} entity object from DB by its id and user
     *
     * @param userId - primary key to search {@link User} entity object in DB
     * @param orderId - primary key to search {@link Order} entity object in DB
     * @return {@link Order} entity object from DB
     */
    @GetMapping(value = "/{userId}/{orderId}")
    public OrderDto getOrderByUserAndId(@PathVariable Long userId, @PathVariable Long orderId) {
        return orderDtoMapper.toDto(service.getOrderByUserAndId(userId, orderId));
    }

    /**
     * Method allows creating a new {@link Order} entity object in DB
     *
     * @param json - tag object to map from request body
     * @return {@link Order} of created entity
     */
    @PostMapping(consumes = "application/json")
    public OrderDto createOrderOnUser(@RequestBody String json) {
        try {
            Order order = objectMapper.readValue(json, Order.class);
            if (order.getUser() == null || order.getCertificates() == null) {
                throw new InvalidRequestException("Empty field");
            }

            return orderDtoMapper.toDto(service.createOrder(order));
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

    /**
     * Method allows deleting {@link Order} from DB by its id
     *
     * @param id - primary key to search {@link Order} entity object in DB
     */
    @DeleteMapping(value = "/{id}")
    public void deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);
    }

}