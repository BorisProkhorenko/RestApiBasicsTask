package com.epam.esm.model;


import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_gift_certificate")
public class OrderCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name="order_id", nullable=false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name="gift_certificate_id", nullable=false)
    private Certificate certificate;

    @Column(name = "json_certificate_snapshot")
    private String snapshot;


    public OrderCertificate() {
    }

    public OrderCertificate(Order order, Certificate certificate, String snapshot) {
        this.order = order;
        this.certificate = certificate;
        this.snapshot = snapshot;
    }

    public OrderCertificate(long id, Order order, Certificate certificate, String snapshot) {
        this.id = id;
        this.order = order;
        this.certificate = certificate;
        this.snapshot = snapshot;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderCertificate)) return false;

        OrderCertificate that = (OrderCertificate) o;

        if (id != that.id) return false;
        if (!Objects.equals(order, that.order)) return false;
        if (!Objects.equals(certificate, that.certificate)) return false;
        return Objects.equals(snapshot, that.snapshot);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + (certificate != null ? certificate.hashCode() : 0);
        result = 31 * result + (snapshot != null ? snapshot.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderCertificate{" +
                "id=" + id +
                ", order=" + order +
                ", certificate=" + certificate +
                ", snapshot='" + snapshot + '\'' +
                '}';
    }
}
