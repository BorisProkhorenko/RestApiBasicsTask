package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {

    GiftCertificate getCertificateById(Long id);

    List<GiftCertificate> getAllCertificates();

    List<GiftCertificate> getAllCertificates(Optional<Tag> tag, Optional<String> part,
                                             Optional<Boolean> nameDesc,
                                             Optional<Boolean> descriptionDesc);

    void deleteCertificate(GiftCertificate certificate);

    GiftCertificate updateCertificate(GiftCertificate certificate);

    GiftCertificate createCertificate(GiftCertificate certificate);

    void addTag(Tag tag, GiftCertificate certificate);

    void removeTag(Tag tag, GiftCertificate certificate);


}
