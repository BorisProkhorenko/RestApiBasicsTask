package com.epam.esm.repo;


import com.epam.esm.config.RepoApplication;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.exceptions.CertificateNotFoundException;

import com.epam.esm.model.Certificate;
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
public class CertificateDaoTest {

    @Autowired
    private CertificateDao dao;


    @Test
    public void testCreateAndGetByIdOk() {
        //given
        Certificate certificate = new Certificate("name", "description", 3, 5);
        //when
        dao.create(certificate);
        Certificate certificateFromDb = dao.getById(1L);
        //then
        Assertions.assertEquals("name", certificateFromDb.getName());
    }

    @Test
    public void testCreateAndGetAllOk() {
        //given
        Certificate certificate = new Certificate("name", "description", 3, 5);
        //when
        dao.create(certificate);
        List<Certificate> certificates = dao.getAll(0,5);
        //then
        Assertions.assertEquals(1, certificates.size());
    }

    @Test()
    public void testCreateAndDeleteByIdThrowsException() {
        //given
        Certificate certificate = new Certificate("name", "description", 3, 5);
        //when
        dao.create(certificate);
        certificate.setId(1L);
        dao.delete(certificate);
        //then
        Assertions.assertThrows(CertificateNotFoundException.class, () ->
                dao.getById(1L));
    }

    @Test
    public void testCreateThenUpdateAndGetByIdOk() {
        //given
        Certificate certificate = new Certificate("name", "description", 3, 5);
        Certificate updated = new Certificate(1L, "updated", null, 3, 5);
        //when
        dao.create(certificate);
        dao.update(updated);
        Certificate certificateFromDb = dao.getById(1L);
        //then
        Assertions.assertEquals("updated", certificateFromDb.getName());
        Assertions.assertEquals("description", certificateFromDb.getDescription());
    }

    @Test
    public void testCreateAndUpdateException() {
        //given
        Certificate certificate = new Certificate("name", "description", 3, 5);
        Certificate updated = new Certificate("updated", "description", 3, 5);
        //when
        dao.create(certificate);
        //then
        Assertions.assertThrows(CertificateNotFoundException.class, () ->
                dao.update(updated));
    }

    @Test
    public void testCreateWithTagsGetByIdOk() {
        //given
        Certificate certificate = new Certificate("name", "description", 3, 5);
        Set<com.epam.esm.model.Tag> tags = new HashSet<>();
        tags.add(new Tag("tag"));
        certificate.setTags(tags);
        //when
        dao.create(certificate);
        Certificate certificateFromDB = dao.getById(1L);
        //then
        Assertions.assertEquals(1, certificateFromDB.getTags().size());
    }

    @Test
    public void testCreateAndUpdateWithTagsGetByIdOk() {
        //given
        Certificate certificate = new Certificate("name", "description", 3, 5);
        Certificate updated = new Certificate(1L,"updated", "updated", 3, 5);
        Set<com.epam.esm.model.Tag> tags = new HashSet<>();
        Set<com.epam.esm.model.Tag> tagsUpdated = new HashSet<>();
        tags.add(new Tag("tag"));
        tagsUpdated.add(new Tag("tag1"));
        tagsUpdated.add(new Tag("tag2"));
        certificate.setTags(tags);
        updated.setTags(tagsUpdated);
        //when
        dao.create(certificate);
        System.out.println(dao.getAll(0,5));
        dao.update(updated);
        Certificate certificateFromDB = dao.getById(1L);
        //then
        Assertions.assertEquals(2, certificateFromDB.getTags().size());
    }






}
