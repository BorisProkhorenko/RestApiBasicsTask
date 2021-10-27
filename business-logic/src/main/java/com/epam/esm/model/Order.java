package com.epam.esm.model;


import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn (name="user_id", nullable=false)
    private User user;


    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderCertificate> snapshots;


    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name = "order_gift_certificate", joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_certificate_id"))
    private List<Certificate> certificates;


    public Order() {
    }

    public Order(long id, User user, List<Certificate> certificates) {
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderCertificate> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<OrderCertificate> snapshots) {
        this.snapshots = snapshots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        Order order = (Order) o;

        if (id != order.id) return false;
        if (!Objects.equals(user, order.user)) return false;
        if (!Objects.equals(updateDate, order.updateDate)) return false;
        return Objects.equals(certificates, order.certificates);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (updateDate != null ? updateDate.hashCode() : 0);
        result = 31 * result + (certificates != null ? certificates.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Order{");
        builder.append("id=").append(id);
        if(user != null){
            builder.append(", user id=").append(user.getId());
            if(user.getUsername()!=null){
                builder.append(", user=").append(user.getUsername());
            }
        }
        builder.append(", updateDate=").append(updateDate);
        builder.append(", certificates=").append(certificates);
        builder.append('}');
        return builder.toString();
    }
}
