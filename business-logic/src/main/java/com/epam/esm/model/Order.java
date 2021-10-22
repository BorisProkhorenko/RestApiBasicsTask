package com.epam.esm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "`order`")
public class Order {

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn (name="user_id", nullable=false)
    private User user;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name = "order_gift_certificate", joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_certificate_id"))
    private Set<GiftCertificate> certificates;

    public Order() {
    }

    public Order(long id, User user, Set<GiftCertificate> certificates) {
        this.id = id;
        this.user = user;
        this.certificates = certificates;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<GiftCertificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(Set<GiftCertificate> certificates) {
        this.certificates = certificates;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        Order order = (Order) o;

        if (id != order.id) return false;
        if (!Objects.equals(user, order.user)) return false;
        return Objects.equals(certificates, order.certificates);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (certificates != null ? certificates.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user id=" + user.getId() +
                ", user=" + user.getUsername() +
                ", certificates=" + certificates +
                '}';
    }
}
