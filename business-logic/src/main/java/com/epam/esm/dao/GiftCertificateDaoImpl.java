package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {


    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> mapper;

    private final String SQL_FIND_CERTIFICATE = "select * from gift_certificate where id = ?";
    private final String SQL_DELETE_CERTIFICATE = "delete from gift_certificate where id = ?";
    private final String SQL_GET_ALL = "select * from gift_certificate";
    private final String SQL_INSERT_CERTIFICATE = "insert into gift_certificate(name, description," +
            " price, duration) values(?,?,?,?)";
    private final String SQL_UPDATE_CERTIFICATE = "update gift_certificate set name = ?," +
            " description = ?, price  = ?, duration = ? where id = ?";

    @Autowired
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
        return jdbcTemplate.query(SQL_GET_ALL, mapper);
    }


    @Override
    public void deleteCertificate(GiftCertificate certificate) {
        jdbcTemplate.update(SQL_DELETE_CERTIFICATE, certificate.getId());
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
}
