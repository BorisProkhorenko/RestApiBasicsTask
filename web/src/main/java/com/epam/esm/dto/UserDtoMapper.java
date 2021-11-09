package com.epam.esm.dto;

import com.epam.esm.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDtoMapper {

    private final OrderDtoMapper orderDtoMapper;

    public UserDtoMapper(OrderDtoMapper orderDtoMapper) {
        this.orderDtoMapper = orderDtoMapper;
    }

    public UserDto toDto(User user) {

        Set<OrderDto> orderDtoSet = user.getOrders()
                .stream()
                .map(orderDtoMapper::toDto)
                .collect(Collectors.toSet());

        return new UserDto(user.getId(), user.getUsername(), orderDtoSet);
    }
}
