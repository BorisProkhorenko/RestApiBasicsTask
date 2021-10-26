package com.epam.esm.dto;

import com.epam.esm.model.Order;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

public class UserDto {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long id;

    private String username;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<OrderDto> orderDtoSet;

    public UserDto() {
    }

    public UserDto(long id, String username, Set<OrderDto> orderDtoSet) {
        this.id = id;
        this.username = username;
        this.orderDtoSet = orderDtoSet;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<OrderDto> getOrderDtoSet() {
        return orderDtoSet;
    }

    public void setOrderDtoSet(Set<OrderDto> orderDtoSet) {
        this.orderDtoSet = orderDtoSet;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", orderDtoSet=" + orderDtoSet +
                '}';
    }
}
