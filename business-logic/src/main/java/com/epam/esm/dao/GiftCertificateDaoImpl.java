package com.epam.esm.dao;

import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.List;


@Repository
@Transactional
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private static final Logger LOGGER = LogManager.getLogger();

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> mapper;
    private final TagDao tagDao;

    private final static String SQL_FIND_CERTIFICATE = "select gc.id as gc_id, gc.name as gc_name," +
            "description, price, duration, create_date, last_update_date, tag_id, tag.name as tag_name" +
            " from gift_certificate as gc left join tag_gift_certificate as tgc on " +
            "gc.id = tgc.gift_certificate_id left join tag on tgc.tag_id = tag.id where gc.id = ?";

    private final static String SQL_DELETE_CERTIFICATE = "delete from gift_certificate where id = ?";

    private final static String SQL_GET_ALL = "select gc.id as gc_id, gc.name as gc_name," +
            "description, price, duration, create_date, last_update_date, tag_id, tag.name as tag_name" +
            " from gift_certificate as gc left join tag_gift_certificate as tgc on " +
            "gc.id = tgc.gift_certificate_id left join tag on tgc.tag_id = tag.id order by gc_id";

    private final static String SQL_INSERT_CERTIFICATE = "insert into gift_certificate(name, description," +
            " price, duration) values(?,?,?,?)";

    private final static String SQL_UPDATE_CERTIFICATE = "update gift_certificate set name = coalesce(?,name)," +
            " description = coalesce(?,description), price = coalesce(?,price), duration = coalesce(?,duration)" +
            " where id = ?";

    private final static String SQL_INSERT_TAG_CERTIFICATE = "insert into tag_gift_certificate(tag_id," +
            " gift_certificate_id) values(?,?)";

    private final static String SQL_DELETE_TAGS_CERTIFICATE = "delete from tag_gift_certificate where gift_certificate_id = ?";

    private final static String SQL_FIND_LAST_CERTIFICATE_ID = "select max(id) from gift_certificate";


    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<GiftCertificate> mapper, TagDao tagDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.tagDao = tagDao;
    }


    @Override
    public GiftCertificate getCertificateById(Long id) {
        GiftCertificate certificate;
        try {
            certificate = jdbcTemplate.queryForObject(SQL_FIND_CERTIFICATE, new Object[]{id}, mapper);
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage(), e);
            throw new CertificateNotFoundException(id);
        }
        return certificate;
    }

    @Override
    public List<GiftCertificate> getAllCertificates() {
        return jdbcTemplate.query(con ->
                con.prepareStatement(SQL_GET_ALL, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE), mapper);

    }

    @Override
    public void deleteCertificateById(Long id) {
        jdbcTemplate.update(SQL_DELETE_CERTIFICATE, id);
    }


    @Override
    public GiftCertificate updateCertificate(GiftCertificate certificate) {
        getCertificateById(certificate.getId());
        jdbcTemplate.update(SQL_UPDATE_CERTIFICATE, certificate.getName(),
                certificate.getDescription(), certificate.getPrice(), certificate.getDuration(),
                certificate.getId());
        removeTags(certificate);
        createTags(certificate);
        return certificate;
    }


    @Override
    public GiftCertificate createCertificate(GiftCertificate certificate) {
        jdbcTemplate.update(SQL_INSERT_CERTIFICATE,
                certificate.getName(), certificate.getDescription(), certificate.getPrice(),
                certificate.getDuration());
        certificate.setId(getCreatedCertificateId());

        createTags(certificate);

        return certificate;
    }

    private long getCreatedCertificateId() {
        RowMapper<Long> rowMapper = (resultSet, i) -> resultSet.getLong(1);
        return jdbcTemplate.queryForObject(SQL_FIND_LAST_CERTIFICATE_ID, new Object[]{}, rowMapper);
    }

    private void createTags(GiftCertificate certificate) {
        if (certificate.getTags() != null) {

            for (Tag tag : certificate.getTags()) {
                tagDao.createTag(tag);
                addTag(certificate.getId(), tag.getName());
            }
        }
    }


    private void addTag(Long id, String tagName) {
        GiftCertificate certificate = getCertificateById(id);
        Tag tag = tagDao.getTagByName(tagName);
        boolean isTagAssociated = certificate.getTags().stream().anyMatch(t -> t.getId() == tag.getId());
        if (isTagAssociated) {
            return;
        }
        jdbcTemplate.update(SQL_INSERT_TAG_CERTIFICATE,
                tag.getId(), id);
    }


    private void removeTags(GiftCertificate certificate) {
        jdbcTemplate.update(SQL_DELETE_TAGS_CERTIFICATE,
                certificate.getId());
    }

}
