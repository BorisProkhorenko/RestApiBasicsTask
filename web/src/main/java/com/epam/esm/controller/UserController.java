package com.epam.esm.controller;


import com.epam.esm.dto.*;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
public class UserController extends PaginatedController<UserController, UserDto, User> {

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
    @PreAuthorize("#oauth2.hasScope('write') and hasRole('ROLE_ADMIN')")
    public UserDto getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = service.findById(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException(id);
        }
        UserDto user = userDtoMapper.toDto(optionalUser.get());
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
     * @param pageNum - page of displayed dto objects
     * @param size    - count of displayed dto objects
     * @return {@link CollectionModel} of {@link UserDto} of entity objects from DB without orders.
     */
    @Override
    @GetMapping(produces = {"application/hal+json"})
    @PreAuthorize("#oauth2.hasScope('write') and hasRole('ROLE_ADMIN')")
    public CollectionModel<UserDto> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int pageNum,
                                           @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        Page<User> page = service.findAll(pageNum, size);
        List<UserDto> users = getUsersFromPage(page);
        buildUsersLinks(users);
        List<Link> links = buildPagination(page, UserController.class);
        Link selfLink = linkTo(UserController.class).withSelfRel();
        links.add(selfLink);
        return CollectionModel.of(users, links);
    }

    /**
     * Method allows getting {@link User} from DB by authentication
     *
     * @param authentication - authentication of actual user
     * @return {@link UserDto} of entity object from DB
     */
    @GetMapping(value = "self",produces = {"application/hal+json"})
    @PreAuthorize("#oauth2.hasScope('trust')")
    public UserDto getSelf(Authentication authentication) {
        User user = getUserByAuthentication(authentication);
        return getUserById(user.getId());
    }

    /**
     * Method allows getting {@link Order} entity object from DB by its id and user
     *
     * @param userId  - primary key to search {@link User} entity object in DB
     * @param orderId - primary key to search {@link Order} entity object in DB
     * @return {@link OrderDto} of entity object from DB
     */
    @GetMapping(value = "/{userId}/{orderId}")
    @PreAuthorize("#oauth2.hasScope('write') and hasRole('ROLE_ADMIN')")
    public OrderDto getOrderByUserAndId(@PathVariable Long userId, @PathVariable Long orderId) {
        OrderDto order = orderDtoMapper.toDto(service.getOrderByUserAndId(userId, orderId));
        buildOrderLinks(order);
        return order;
    }

    /**
     * Method allows getting {@link Order} entity object from DB by its id and user(from authentication)
     *
     * @param authentication - authentication of actual user
     * @param orderId - primary key to search {@link Order} entity object in DB
     * @return {@link OrderDto} of entity object from DB
     */
    @GetMapping(value = "/self/{orderId}")
    @PreAuthorize("#oauth2.hasScope('trust')")
    public OrderDto getOrderById(Authentication authentication,@PathVariable Long orderId) {
        User user = getUserByAuthentication(authentication);
        long userId = user.getId();
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
    @PreAuthorize("#oauth2.hasScope('trust')")
    public OrderDto createOrderOnUser(Authentication authentication, @RequestBody String json) {
        Order order = readOrderFromJson(json);
        User user = getUserByAuthentication(authentication);
        order.setUser(user);
        OrderDto orderDto = orderDtoMapper.toDto(service.createOrder(order));
        buildOrderLinks(orderDto);
        return orderDto;
    }


    /**
     * Method allows deleting {@link Order} from DB by its id
     *
     * @param id - primary key to search {@link Order} entity object in DB
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("#oauth2.hasScope('write') and hasRole('ROLE_ADMIN')")
    public void deleteOrder(@PathVariable Long id) {
        service.deleteOrder(new Order(id));
    }


    @Override
    public CollectionModel<UserDto> getAll(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                           @RequestParam(name = "size", required = false, defaultValue = "20") int size,
                                           @RequestParam(name = "filter_by_tags") Optional<String> tags,
                                           @RequestParam(name = "filter_by_part") Optional<String> part,
                                           @RequestParam(name = "sort_by", required = false) String sort) {
        return getAll(page, size);
    }

    private User getUserByAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return service.findUserByUsername(username);
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

    private List<UserDto> getUsersFromPage(Page<User> page) {
        return page.getContent()
                .stream()
                .peek(user -> user.setOrders(new HashSet<>()))
                .map(userDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    private void buildUsersLinks(List<UserDto> users) {
        for (UserDto user : users) {
            Long id = user.getId();
            Link selfLink = linkTo(UserController.class).slash(id).withSelfRel();
            user.add(selfLink);
        }
    }

    private Order readOrderFromJson(String json) {
        try {
            Order order = objectMapper.readValue(json, Order.class);
            if (order.getCertificates() == null) {
                throw new InvalidRequestException("Empty  certificates");
            }
            return order;
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }


}