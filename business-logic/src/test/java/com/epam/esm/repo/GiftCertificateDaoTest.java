package com.epam.esm.repo;

import com.epam.esm.config.AppConfig;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.Set;


@ContextConfiguration(
        classes = {AppConfig.class, GiftCertificateDao.class},
        loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("test")
public class GiftCertificateDaoTest {

    private final static String H2_CREATE_CERTIFICATE = "create table gift_certificate\n" +
            "(\n" +
            "    id               bigint NOT NULL AUTO_INCREMENT primary key,\n" +
            "    name             varchar(255),\n" +
            "    description      varchar(7000),\n" +
            "    price            double,\n" +
            "    duration         int,\n" +
            "    create_date      timestamp DEFAULT CURRENT_TIMESTAMP,\n" +
            "    last_update_date timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP\n" +
            ")";

    private final static String H2_CREATE_TAG = "create table tag\n" +
            "(\n" +
            "    id            bigint NOT NULL AUTO_INCREMENT primary key,\n" +
            "    name          varchar(255)\n" +
            "\n" +
            ")";

    private final static String H2_CREATE_TAG_CERTIFICATE = "create table tag_gift_certificate\n" +
            "(\n" +
            "    id                        bigint NOT NULL AUTO_INCREMENT primary key,\n" +
            "    tag_id                    bigint,\n" +
            "    gift_certificate_id       bigint\n" +
            "\n" +
            ")";

    private final static String H2_ADD_TAG_FOREIGN_KEY = "alter table tag_gift_certificate\n" +
            "    add foreign key (tag_id) references tag (id) ON DELETE CASCADE";
    private final static String H2_ADD_CERTIFICATE_FOREIGN_KEY = "alter table tag_gift_certificate\n" +
            "    add foreign key(gift_certificate_id) references gift_certificate(id) ON DELETE CASCADE";

    private final static String H2_DROP_TAG_CERTIFICATE ="drop table tag_gift_certificate";
    private final static String H2_DROP_CERTIFICATE ="drop table gift_certificate";
    private final static String H2_DROP_TAG ="drop table tag";


    private static GiftCertificateDao dao;
    private static TagDao tagDao;
    private static JdbcTemplate jdbcTemplate;

    @BeforeAll
    public static void init() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        jdbcTemplate = context.getBean(JdbcTemplate.class);
        dao = context.getBean(GiftCertificateDao.class);
        tagDao = context.getBean(TagDao.class);
    }

    @BeforeEach
    public void before() {
        jdbcTemplate.execute(H2_CREATE_CERTIFICATE);
        jdbcTemplate.execute(H2_CREATE_TAG);
        jdbcTemplate.execute(H2_CREATE_TAG_CERTIFICATE);
        jdbcTemplate.execute(H2_ADD_TAG_FOREIGN_KEY);
        jdbcTemplate.execute(H2_ADD_CERTIFICATE_FOREIGN_KEY);
    }

    @AfterEach
    public void afterEach(){
        jdbcTemplate.execute(H2_DROP_TAG_CERTIFICATE);
        jdbcTemplate.execute(H2_DROP_CERTIFICATE);
        jdbcTemplate.execute(H2_DROP_TAG);

    }

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
        GiftCertificate updated = new GiftCertificate(1L,"updated", "description", 3, 5);
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
        dao.addTag(1L,1L);
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
                dao.addTag(1L,1L));
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
        dao.addTag(1L,1L);
        dao.removeTag(1L,1L);
        GiftCertificate certificateFromDb = dao.getCertificateById(1L);
        Set<Tag> tags = certificateFromDb.getTags();
        //then
        Assertions.assertEquals(0, tags.size());
    }


}
