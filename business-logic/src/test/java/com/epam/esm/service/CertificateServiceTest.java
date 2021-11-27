package com.epam.esm.service;

import com.epam.esm.config.RepoApplication;
import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RepoApplication.class)
@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-test.properties")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Transactional
public class CertificateServiceTest {


    @Autowired
    private CertificateService service;


    @Test
    public void testCreateAndGetByIdOk() {
        //given
        Certificate certificate = new Certificate("name", "description", 3D, 5);
        //when
        service.create(certificate);
        Optional<Certificate> optionalCertificate = service.findById(1L);
        //then
        Assertions.assertEquals("name", optionalCertificate.get().getName());
    }

    @Test
    public void testCreateAndGetAllOk() {
        //given
        Certificate certificate = new Certificate("name", "description", 3D, 5);
        //when
        service.create(certificate);
        Page<Certificate> certificates = service.findAll(0, 5);
        //then
        Assertions.assertEquals(1, certificates.getContent().size());
    }

    @Test()
    public void testCreateAndDeleteByIdEmpty() {
        //given
        Certificate certificate = new Certificate("name", "description", 3D, 5);
        //when
        service.create(certificate);
        certificate.setId(1L);
        service.delete(certificate);
        Optional<Certificate> fromDb = service.findById(1L);
        //then
        Assertions.assertFalse(fromDb.isPresent());
    }

    @Test
    public void testCreateThenUpdateAndGetByIdOk() {
        //given
        Certificate certificate = new Certificate("name", "description", 3D, 5);
        Certificate updated = new Certificate(1L, "updated", null, 3D, 5);
        //when
        service.create(certificate);
        service.update(updated);
        Optional<Certificate> certificateFromDb = service.findById(1L);
        //then
        Assertions.assertEquals("updated", certificateFromDb.get().getName());
        Assertions.assertEquals("description", certificateFromDb.get().getDescription());
    }

    @Test
    public void testCreateAndUpdateException() {
        //given
        Certificate certificate = new Certificate("name", "description", 3D, 5);
        Certificate updated = new Certificate("updated", "description", 3D, 5);
        //when
        service.create(certificate);
        //then
        Assertions.assertThrows(CertificateNotFoundException.class, () ->
                service.update(updated));
    }

    @Test
    public void testCreateWithTagsGetByIdOk() {
        //given
        Certificate certificate = new Certificate("name", "description", 3D, 5);
        Set<com.epam.esm.model.Tag> tags = new HashSet<>();
        tags.add(new Tag("tag"));
        certificate.setTags(tags);
        //when
        service.create(certificate);
        Optional<Certificate> certificateFromDB = service.findById(1L);
        //then
        Assertions.assertEquals(1, certificateFromDB.get().getTags().size());
    }

    @Test
    public void testCreateAndUpdateWithTagsGetByIdOk() {
        //given
        Certificate certificate = new Certificate("name", "description", 3D, 5);
        Certificate updated = new Certificate(1L, "updated", "updated", 3D, 5);
        Set<com.epam.esm.model.Tag> tags = new HashSet<>();
        Set<com.epam.esm.model.Tag> tagsUpdated = new HashSet<>();
        tags.add(new Tag("tag"));
        tagsUpdated.add(new Tag("tag1"));
        tagsUpdated.add(new Tag("tag2"));
        certificate.setTags(tags);
        updated.setTags(tagsUpdated);
        //when
        service.create(certificate);
        service.update(updated);
        Optional<Certificate> certificateFromDB = service.findById(1L);
        //then
        Assertions.assertEquals(2, certificateFromDB.get().getTags().size());
    }

    @Test
    public void testCreateAndGetAllSpecificationOk() {
        //given
        Certificate certificate = new Certificate("name", "description", 3D, 5);
        Certificate certificate1 = new Certificate("1", "2", 3D, 5);
        CertificateSpecificationsBuilder builder = new CertificateSpecificationsBuilder();
        Specification<Certificate> specification = builder
                .with(SearchCriteria.Operation.PART, "na")
                .build();
        //when
        service.create(certificate);
        service.create(certificate1);
        Page<Certificate> certificates = service.findAll(specification,new HashMap<>(),0, 5);
        //then
        Assertions.assertEquals(1, certificates.getContent().size());
    }

    @Test
    public void testCreateAndGetAllSortOk() {
        //given
        Certificate certificate = new Certificate("name", "description", 3D, 5);
        Certificate certificate1 = new Certificate("1", "2", 3D, 5);
        Map<String,String> map = new HashMap<>();
        map.put("name","asc");
        //when
        service.create(certificate);
        service.create(certificate1);
        Page<Certificate> certificates = service.findAll(null,map,0, 5);
        Certificate fromDb = certificates.getContent().get(0);
        //then
        Assertions.assertEquals(certificate1, fromDb);
    }

}
