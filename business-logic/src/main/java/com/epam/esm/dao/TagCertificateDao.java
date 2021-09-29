package com.epam.esm.dao;


import com.epam.esm.model.TagCertificate;

import java.util.List;

public interface TagCertificateDao {

    TagCertificate getTagCertificateById(Long id);

    List<TagCertificate> getAllTagCertificates();

    boolean deleteTagCertificate(TagCertificate tagCertificate);

    boolean createTagCertificate(TagCertificate tagCertificate);

}
