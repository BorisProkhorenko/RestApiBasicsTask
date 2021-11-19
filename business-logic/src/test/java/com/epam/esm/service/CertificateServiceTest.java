package com.epam.esm.service;

import com.epam.esm.config.RepoApplication;
import com.epam.esm.repository.dao.CertificateDao;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RepoApplication.class)
@ActiveProfiles("test")
public class CertificateServiceTest {

    private static CertificateService service;
    private final static String MOCK = "mock";
    private final static Certificate MOCK_CERTIFICATE =
            new Certificate(MOCK, MOCK, 3, 3);

    @BeforeAll
    public static void init() {
        CertificateDao mockDao = Mockito.mock(CertificateDao.class);
        when(mockDao.getById(anyLong()))
                .thenReturn(MOCK_CERTIFICATE);
        when(mockDao.create(any()))
                .thenReturn(MOCK_CERTIFICATE);
        when(mockDao.update(any()))
                .thenReturn(MOCK_CERTIFICATE);
        Certificate certificate1 = new Certificate("name", "description", 4, 4);
        Tag tag = new Tag(1L, "tag");
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);
        certificate1.setTags(tagSet);
        Set<Tag> emptySet = new HashSet<>();
        MOCK_CERTIFICATE.setTags(emptySet);
        Certificate certificate2 = new Certificate("test", "test", 4, 4);
        certificate2.setTags(emptySet);

        List<Certificate> certificates = Arrays.asList(MOCK_CERTIFICATE, certificate2, certificate1);
        when(mockDao.getAll(anyInt(),anyInt()))
                .thenReturn(certificates);


        service = new CertificateService(mockDao);
    }


    @Test
    public void testGetAll() {
        //when
        List<Certificate> certificates = service.getAll(Optional.empty(),Optional.empty());
        //then
        Assertions.assertNotNull(certificates);
    }

    @Test
    public void testGetById() {
        //when
        Certificate certificate = service.getById(1L);
        //then
        Assertions.assertEquals(certificate, MOCK_CERTIFICATE);
    }

    @Test
    public void testCreate() {
        //when
        Certificate certificate = service.create(MOCK_CERTIFICATE);
        //then
        Assertions.assertEquals(certificate, MOCK_CERTIFICATE);
    }

    @Test
    public void testUpdate() {
        //when
        Certificate certificate = service.update(MOCK_CERTIFICATE);
        //then
        Assertions.assertEquals(certificate, MOCK_CERTIFICATE);
    }


}
