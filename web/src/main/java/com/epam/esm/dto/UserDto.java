package com.epam.esm.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Relation(collectionRelation = "users", itemRelation = "user")
public class UserDto extends RepresentationModel<UserDto> {

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


}
