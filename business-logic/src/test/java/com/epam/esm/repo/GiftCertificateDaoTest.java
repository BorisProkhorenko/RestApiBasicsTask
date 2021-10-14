package com.epam.esm.repo;


import com.epam.esm.config.AppConfig;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class GiftCertificateDaoTest {

    @Autowired
    private GiftCertificateDao dao;
    @Autowired
    private TagDao tagDao;


    @Test
    public void testCreateAndGetByIdOk() {
        //given
        GiftCertificate certificate = new GiftCertificate("name", "description", 3, 5);
        //when
        dao.createCertificate(certificate);
        GiftCertificate certificateFromDb = dao.getCertificateById(1L);
        //then
        Assertions.assertEquals("name", certificateFromDb.getName());
    }

    @Test
    public void testCreateAndGetAllOk() {
        //given
        GiftCertificate certificate = new GiftCertificate("name", "description", 3, 5);
        //when
        dao.createCertificate(certificate);
        List<GiftCertificate> certificates = dao.getAllCertificates();
        //then
        Assertions.assertEquals(1, certificates.size());
    }

    @Test()
    public void testCreateAndDeleteByIdThrowsException() {
        //given
        GiftCertificate certificate = new GiftCertificate("name", "description", 3, 5);
        //when
        dao.createCertificate(certificate);
        dao.deleteCertificateById(1L);
        //then
        Assertions.assertThrows(CertificateNotFoundException.class, () ->
                dao.getCertificateById(1L));
    }

    @Test
    public void testCreateThenUpdateAndGetByIdOk() {
        //given
        GiftCertificate certificate = new GiftCertificate("name", "description", 3, 5);
        GiftCertificate updated = new GiftCertificate(1L, "updated", "description", 3, 5);
        //when
        dao.createCertificate(certificate);
        dao.updateCertificate(updated);
        GiftCertificate certificateFromDb = dao.getCertificateById(1L);
        //then
        Assertions.assertEquals("updated", certificateFromDb.getName());
    }

    @Test
    public void testCreateAndUpdateException() {
        //given
        GiftCertificate certificate = new GiftCertificate("name", "description", 3, 5);
        GiftCertificate updated = new GiftCertificate("updated", "description", 3, 5);
        //when
        dao.createCertificate(certificate);
        //then
        Assertions.assertThrows(CertificateNotFoundException.class, () ->
                dao.updateCertificate(updated));
    }


    @Test
    public void testCreateAddTagGetOk() {
        //given
        GiftCertificate certificate = new GiftCertificate("name", "description", 3, 5);
        Tag tag = new Tag("TagName");
        //when
        dao.createCertificate(certificate);
        tagDao.createTag(tag);
        dao.addTag(1L, 1L);
        GiftCertificate certificateFromDb = dao.getCertificateById(1L);
        Set<Tag> tags = certificateFromDb.getTags();
        //then
        Assertions.assertEquals(1, tags.size());
    }

    @Test
    public void testCreateAddTagGetTagNotFoundException() {
        //given
        GiftCertificate certificate = new GiftCertificate("name", "description", 3, 5);
        dao.createCertificate(certificate);
        //then
        Assertions.assertThrows(TagNotFoundException.class, () ->
                dao.addTag(1L, 1L));
    }

    @Test
    public void testCreateAddTagGetCertificateNotFoundException() {
        //given
        Tag tag = new Tag("TagName");
        tagDao.createTag(tag);
        //then
        Assertions.assertThrows(CertificateNotFoundException.class, () ->
                dao.addTag(1L, 1L));
    }

    @Test
    public void testCreateAddTagDeleteGetOk() {
        //given
        GiftCertificate certificate = new GiftCertificate("name", "description", 3, 5);
        Tag tag = new Tag("TagName");
        //when
        dao.createCertificate(certificate);
        tagDao.createTag(tag);
        dao.addTag(1L, 1L);
        dao.removeTag(1L, 1L);
        GiftCertificate certificateFromDb = dao.getCertificateById(1L);
        Set<Tag> tags = certificateFromDb.getTags();
        //then
        Assertions.assertEquals(0, tags.size());
    }


}
