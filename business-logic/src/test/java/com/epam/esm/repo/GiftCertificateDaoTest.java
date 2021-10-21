package com.epam.esm.repo;


import com.epam.esm.config.RepoApplication;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.exceptions.CertificateNotFoundException;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;


import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RepoApplication.class)
@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-test.properties")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Transactional
public class GiftCertificateDaoTest {

    @Autowired
    private GiftCertificateDao dao;


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
        certificate.setId(1L);
        dao.deleteCertificate(certificate);
        //then
        Assertions.assertThrows(CertificateNotFoundException.class, () ->
                dao.getCertificateById(1L));
    }

    @Test
    public void testCreateThenUpdateAndGetByIdOk() {
        //given
        GiftCertificate certificate = new GiftCertificate("name", "description", 3, 5);
        GiftCertificate updated = new GiftCertificate(1L, "updated", null, 3, 5);
        //when
        dao.createCertificate(certificate);
        dao.updateCertificate(updated);
        GiftCertificate certificateFromDb = dao.getCertificateById(1L);
        //then
        Assertions.assertEquals("updated", certificateFromDb.getName());
        Assertions.assertEquals("description", certificateFromDb.getDescription());
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
    public void testCreateWithTagsGetByIdOk() {
        //given
        GiftCertificate certificate = new GiftCertificate("name", "description", 3, 5);
        Set<com.epam.esm.model.Tag> tags = new HashSet<>();
        tags.add(new Tag("tag"));
        certificate.setTags(tags);
        //when
        dao.createCertificate(certificate);
        GiftCertificate certificateFromDB = dao.getCertificateById(1L);
        //then
        Assertions.assertEquals(1, certificateFromDB.getTags().size());
    }

    @Test
    public void testCreateAndUpdateWithTagsGetByIdOk() {
        //given
        GiftCertificate certificate = new GiftCertificate("name", "description", 3, 5);
        GiftCertificate updated = new GiftCertificate(1L,"updated", "updated", 3, 5);
        Set<com.epam.esm.model.Tag> tags = new HashSet<>();
        Set<com.epam.esm.model.Tag> tagsUpdated = new HashSet<>();
        tags.add(new Tag("tag"));
        tagsUpdated.add(new Tag("tag1"));
        tagsUpdated.add(new Tag("tag2"));
        certificate.setTags(tags);
        updated.setTags(tagsUpdated);
        //when
        dao.createCertificate(certificate);
        System.out.println(dao.getAllCertificates());
        dao.updateCertificate(updated);
        GiftCertificate certificateFromDB = dao.getCertificateById(1L);
        //then
        Assertions.assertEquals(2, certificateFromDB.getTags().size());
    }






}
