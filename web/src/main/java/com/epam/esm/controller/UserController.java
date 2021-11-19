package com.epam.esm.controller;


import com.epam.esm.dto.*;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * This Controller provides public API for operations with {@link User} and {@link Order} entity.
 * Uses {@link UserService} to access Data Base through business-logic layer.
 * Uses {@link ObjectMapper} to map objects from JSON
 * Uses {@link UserDtoMapper} and {@link OrderDtoMapper} to map objects from Entity to Dto
 * Uses {@link PaginatedController} for pagination
 *
 * @author Boris Prokhorenko
 * @see User
 * @see UserDto
 * @see UserDtoMapper
 * @see Order
 * @see OrderDto
 * @see OrderDtoMapper
 * @see UserService
 * @see PaginatedController
 */
@RestController
@RequestMapping(value = "/users")
public class UserController extends PaginatedController<UserController, UserDto, User>{

    private final UserService service;
    private final ObjectMapper objectMapper;
    private final UserDtoMapper userDtoMapper;
    private final OrderDtoMapper orderDtoMapper;

    private final static String USERS = "users";

    public UserController(UserService service, ObjectMapper objectMapper, UserDtoMapper userDtoMapper,
                          OrderDtoMapper orderDtoMapper) {
        super(service);
        this.service = service;
        this.objectMapper = objectMapper;
        this.userDtoMapper = userDtoMapper;
        this.orderDtoMapper = orderDtoMapper;
    }

    /**
     * Method allows getting {@link User} from DB by its id
     *
     * @param id - primary key to search {@link User} entity object in DB
     * @return {@link UserDto} of entity object from DB
     */
    @GetMapping(value = "/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public UserDto getUserById(@PathVariable Long id) {
        UserDto user = userDtoMapper.toDto(service.getById(id));
        for (OrderDto order : user.getOrders()) {
            buildOrderLinks(order);
        }
        Link allUsers = linkTo(UserController.class).withRel(USERS);
        user.add(allUsers);
        Link selfLink = linkTo(UserController.class).slash(id).withSelfRel();
        user.add(selfLink);
        return user;
    }

    /**
     * Method allows getting all {@link User} entity objects from DB
     *
     * @param page - page of displayed dto objects
     * @param size - count of displayed dto objects
     * @return {@link CollectionModel} of {@link UserDto} of entity objects from DB without orders.
     */
    @Override
    @GetMapping(produces = {"application/hal+json"})
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public CollectionModel<UserDto> getAll(@RequestParam(name = "page") Optional<Integer> page,
                                           @RequestParam(name = "size") Optional<Integer> size) {

        List<UserDto> users = service.getAll(page, size)
                .stream()
                .peek(user -> user.setOrders(new HashSet<>()))
                .map(userDtoMapper::toDto)
                .collect(Collectors.toList());

        for (UserDto user : users) {
            Long id = user.getId();
            Link selfLink = linkTo(UserController.class).slash(id).withSelfRel();
            user.add(selfLink);
        }
        List<Link> links = buildPagination(page, size, UserController.class);
        Link selfLink = linkTo(UserController.class).withSelfRel();
        links.add(selfLink);
        return CollectionModel.of(users, links);
    }

    /**
     * Method allows getting {@link Order} entity object from DB by its id and user
     *
     * @param userId  - primary key to search {@link User} entity object in DB
     * @param orderId - primary key to search {@link Order} entity object in DB
     * @return {@link OrderDto} of entity object from DB
     */
    @GetMapping(value = "/{userId}/{orderId}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public OrderDto getOrderByUserAndId(@PathVariable Long userId, @PathVariable Long orderId) {
        OrderDto order = orderDtoMapper.toDto(service.getOrderByUserAndId(userId, orderId));
        buildOrderLinks(order);
        return order;
    }

    /**
     * Method allows creating a new {@link Order} entity object in DB
     *
     * @param json - tag object to map from request body
     * @return {@link OrderDto} of created entity
     */
    @PostMapping(consumes = "application/json")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public OrderDto createOrderOnUser(@RequestBody String json) {
        try {
            Order order = objectMapper.readValue(json, Order.class);
            if (order.getUser() == null || order.getCertificates() == null) {
                throw new InvalidRequestException("Empty user or certificates");
            }
            OrderDto orderDto = orderDtoMapper.toDto(service.createOrder(order));
            buildOrderLinks(orderDto);
            return orderDto;
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
    @Secured("ROLE_ADMIN")
    public void deleteOrder(@PathVariable Long id) {
        service.deleteOrder(new Order(id));
    }


    @Override
    public CollectionModel<UserDto> getAll(@RequestParam(name = "page") Optional<Integer> page,
                                           @RequestParam(name = "size") Optional<Integer> size,
                                           @RequestParam(name = "filter_by_tags") Optional<String> tags,
                                           @RequestParam(name = "filter_by_part") Optional<String> part,
                                           @RequestParam(name = "sort_by_name") Optional<String> name,
                                           @RequestParam(name = "sort_by_date") Optional<String> date) {
        return getAll(page,size);
    }

    private void buildOrderLinks(OrderDto order) {
        Long userId = order.getUser().getId();
        Long orderId = order.getId();
        Link userLink = linkTo(UserController.class).slash(userId).withSelfRel();
        order.add(userLink);
        CertificateController.buildCertificateCollectionLinks(order.getCertificates());
        Link allUsers = linkTo(UserController.class).withRel(USERS);
        order.add(allUsers);
        Link selfLink = linkTo(UserController.class).slash(userId).slash(orderId).withSelfRel();
        order.add(selfLink);
    }



}