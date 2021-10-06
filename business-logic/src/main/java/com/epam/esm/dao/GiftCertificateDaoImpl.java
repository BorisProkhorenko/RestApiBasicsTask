package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;


@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {


    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> mapper;

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

    private final static String SQL_UPDATE_CERTIFICATE = "update gift_certificate set name = ?," +
            " description = ?, price = ?, duration =  ? where id = ?";

    private final static String SQL_INSERT_TAG_CERTIFICATE = "insert into tag_gift_certificate(tag_id," +
            " gift_certificate_id, values(?,?)";

    private final static String SQL_DELETE_TAG_CERTIFICATE = "delete from tag_gift_certificate where tag_id = ?" +
            " and gift_certificate_id = ?";


    public GiftCertificateDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<GiftCertificate> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }


    @Override
    public GiftCertificate getCertificateById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_CERTIFICATE, new Object[]{id},
                mapper);
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
        jdbcTemplate.update(SQL_UPDATE_CERTIFICATE, certificate.getName(),
                certificate.getDescription(), certificate.getPrice(), certificate.getDuration(),
                certificate.getId());
        return certificate;
    }


    @Override
    public GiftCertificate createCertificate(GiftCertificate certificate) {
        jdbcTemplate.update(SQL_INSERT_CERTIFICATE,
                certificate.getName(), certificate.getDescription(), certificate.getPrice(),
                certificate.getDuration());
        return certificate;
    }

    @Override
    public void addTag(Long id, Long tagId) {
        jdbcTemplate.update(SQL_INSERT_TAG_CERTIFICATE,
                tagId, id);
    }

    @Override
    public void removeTag(Long id, Long tagId) {
        jdbcTemplate.update(SQL_DELETE_TAG_CERTIFICATE,
                tagId, id);
    }


}
