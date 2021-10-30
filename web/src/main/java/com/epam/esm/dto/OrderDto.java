package com.epam.esm.dto;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

public class OrderDto {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    private User user;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updateDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double cost;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Certificate> certificates;

    public OrderDto() {
    }

    public OrderDto(long id, User user, String updateDate, Double cost, List<Certificate> certificates) {
        this.id = id;
        this.user = user;
        this.updateDate = updateDate;
        this.cost = cost;
        this.certificates = certificates;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Order{");
        builder.append("id=" + id);
        if(user != null){
            builder.append(", user id=" + user.getId());
            if(user.getUsername()!=null){
                builder.append(", user=" + user.getUsername());
            }
        }
        builder.append(", updateDate=" + updateDate);
        builder.append(", cost=" + cost);
        builder.append(", certificates=" + certificates);
        builder.append('}');
        return builder.toString();
    }

}
