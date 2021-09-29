package com.epam.esm.model;

import org.springframework.stereotype.Component;

@Component
public class TagCertificate {

    private long id;
    private GiftCertificate certificate;
    private Tag tag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GiftCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(GiftCertificate certificate) {
        this.certificate = certificate;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "TagCertificate{" +
                "id=" + id +
                ", certificate=" + certificate +
                ", tag=" + tag +
                '}';
    }
}
