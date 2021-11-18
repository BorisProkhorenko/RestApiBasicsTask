package com.epam.esm.model;


import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;


import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "`order`")
@Audited
public class Order implements Identifiable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    private Date updateDate;

    @NotAudited
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderCertificate> snapshots;

    @Transient
    private List<Certificate> certificates;


    private double cost;

    public Order() {
    }

    public Order(long id) {
        this.id = id;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
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
        if (user != null) {
            builder.append(", user id=").append(user.getId());
            if (user.getUsername() != null) {
                builder.append(", user=").append(user.getUsername());
            }
        }
        builder.append(", cost=").append(cost);
        builder.append(", updateDate=").append(updateDate);
        builder.append(", certificates=").append(certificates);
        builder.append('}');
        return builder.toString();
    }
}
