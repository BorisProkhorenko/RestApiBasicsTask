package com.epam.esm.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

public class UserDto {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long id;

    private String username;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<OrderDto> orders;

    public UserDto() {
    }

    public UserDto(long id, String username, Set<OrderDto> orders) {
        this.id = id;
        this.username = username;
        this.orders = orders;
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

    public Set<OrderDto> getOrders() {
        return orders;
    }

    public void setOrders(Set<OrderDto> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", orderDtoSet=" + orders +
                '}';
    }
}
