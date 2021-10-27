package com.epam.esm.service;

import com.epam.esm.config.RepoApplication;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = RepoApplication.class)
public class CertificateServiceTest {

    private static CertificateService service;
    private final static String MOCK = "mock";
    private final static Certificate MOCK_CERTIFICATE =
            new Certificate(MOCK, MOCK, 3, 3);

    @BeforeAll
    public static void init() {
        CertificateDao mockDao = Mockito.mock(CertificateDao.class);
        when(mockDao.getCertificateById(anyLong()))
                .thenReturn(MOCK_CERTIFICATE);
        when(mockDao.createCertificate(any()))
                .thenReturn(MOCK_CERTIFICATE);
        when(mockDao.updateCertificate(any()))
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
        when(mockDao.getAllCertificates())
                .thenReturn(certificates);


        service = new CertificateService(mockDao);
    }


    @Test
    public void testGetAll() {
        //when
        List<Certificate> certificates = service.getAllCertificates();
        //then
        Assertions.assertNotNull(certificates);
    }

    @Test
    public void testGetById() {
        //when
        Certificate certificate = service.getCertificateById(1L);
        //then
        Assertions.assertEquals(certificate, MOCK_CERTIFICATE);
    }

    @Test
    public void testCreate() {
        //when
        Certificate certificate = service.createCertificate(MOCK_CERTIFICATE);
        //then
        Assertions.assertEquals(certificate, MOCK_CERTIFICATE);
    }

    @Test
    public void testUpdate() {
        //when
        Certificate certificate = service.updateCertificate(MOCK_CERTIFICATE);
        //then
        Assertions.assertEquals(certificate, MOCK_CERTIFICATE);
    }

/*
    @Test
    public void testGetWithParamsWhenFilteredByTag() {
        //when
        List<GiftCertificate> certificates = service.getCertificatesWithParams(Optional.of("1"), Optional.empty(),
                Optional.empty(), Optional.empty());
        //then
        Assertions.assertEquals(1, certificates.size());
    }

    @Test
    public void testGetWithParamsWhenFilteredByNoExistedTag() {
        //when
        List<GiftCertificate> certificates = service.getCertificatesWithParams(Optional.of("2"), Optional.empty(),
                Optional.empty(), Optional.empty());
        //then
        Assertions.assertEquals(0, certificates.size());
    }

    @Test
    public void testGetWithParamsWhenFilteredByPart() {
        //when
        List<GiftCertificate> certificates = service.getCertificatesWithParams(Optional.empty(), Optional.of("es"),
                Optional.empty(), Optional.empty());
        //then
        Assertions.assertEquals(2, certificates.size());
    }

    @Test
    public void testGetWithParamsWhenFilteredByNotExistedPart() {
        //when
        List<GiftCertificate> certificates = service.getCertificatesWithParams(Optional.empty(), Optional.of("b"),
                Optional.empty(), Optional.empty());
        //then
        Assertions.assertEquals(0, certificates.size());
    }

    @Test
    public void testGetWithParamsWhenSortedByNameAsc() {
        //when
        List<GiftCertificate> certificates = service.getCertificatesWithParams(Optional.empty(), Optional.empty(),
                Optional.of("asc"), Optional.empty());
        //then
        Assertions.assertEquals(MOCK, certificates.get(0).getName());
    }

    @Test
    public void testGetWithParamsWhenSortedByNameDesc() {
        //when
        List<GiftCertificate> certificates = service.getCertificatesWithParams(Optional.empty(), Optional.empty(),
                Optional.of("desc"), Optional.empty());
        //then
        Assertions.assertEquals("test", certificates.get(0).getName());
    }

    @Test
    public void testGetWithParamsWhenSortedByDescriptionAsc() {
        //when
        List<GiftCertificate> certificates = service.getCertificatesWithParams(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of("asc"));
        //then
        Assertions.assertEquals("description", certificates.get(0).getDescription());
    }

    @Test
    public void testGetWithParamsWhenSortedByDescriptionDesc() {
        //when
        List<GiftCertificate> certificates = service.getCertificatesWithParams(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of("desc"));
        //then
        Assertions.assertEquals("test", certificates.get(0).getDescription());
    }

    @Test
    public void testGetWithParamsWhenSortedByNameDifferent() {
        //when
        Assertions.assertThrows(InvalidRequestException.class, () ->
                service.getCertificatesWithParams(Optional.empty(), Optional.empty(),
                        Optional.of("faldsdse"), Optional.empty()));

    }


    @Test
    public void testGetWithParamsWhenSortedByDescriptionDifferent() {
        //when
        Assertions.assertThrows(InvalidRequestException.class, () ->
                service.getCertificatesWithParams(Optional.empty(), Optional.empty(),
                        Optional.empty(), Optional.of("faldsdse")));

    }


    @Test
    public void testGetWithParamsWhenAllParamsEmpty() {
        //when
        List<GiftCertificate> certificates = service.getCertificatesWithParams(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty());
        //then
        Assertions.assertEquals(MOCK, certificates.get(0).getName());
        Assertions.assertEquals(3, certificates.size());
    }

    @Test
    public void testGetWithParamsWhenSortedByBoth() {
        //when
        List<GiftCertificate> certificates = service.getCertificatesWithParams(Optional.empty(), Optional.empty(),
                Optional.of("asc"), Optional.of("desc"));
        //then
        Assertions.assertEquals("test", certificates.get(0).getDescription());
    }

    @Test
    public void testGetWithParamsWhenFilteredByTagAndPart() {
        //when
        List<GiftCertificate> certificates = service.getCertificatesWithParams(Optional.of("1"), Optional.of("es"),
                Optional.empty(), Optional.empty());
        //then
        Assertions.assertEquals(1, certificates.size());
    }

    @Test
    public void testGetWithParamsWhenInvalidIdArgument() {
        //when
        Assertions.assertThrows(InvalidRequestException.class, () ->
                service.getCertificatesWithParams(Optional.of("1L"), Optional.of("es"),
                        Optional.empty(), Optional.empty()));

    }

 */


}
