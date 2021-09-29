package com.epam.esm.dao;

import com.epam.esm.mapper.GiftCertificateMapper;
import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class GiftCertificateDaoImpl implements GiftCertificateDao {


    private final JdbcTemplate jdbcTemplate;

    private final String SQL_FIND_CERTIFICATE = "select * from gift_certificate where id = ?";
    private final String SQL_DELETE_CERTIFICATE = "delete from gift_certificate where id = ?";
    private final String SQL_GET_ALL = "select * from gift_certificate";
    private final String SQL_INSERT_CERTIFICATE = "insert into gift_certificate(name, description," +
            " price, duration) values(?,?,?,?)";
    private final String SQL_UPDATE_CERTIFICATE = "update gift_certificate set name = ?," +
            " description = ?, price  = ?, duration = ? where id = ?";

    @Autowired
    public GiftCertificateDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public GiftCertificate getCertificateById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_CERTIFICATE, new Object[]{id},
                new GiftCertificateMapper());
    }

    @Override
    public List<GiftCertificate> getAllCertificates() {
        return jdbcTemplate.query(SQL_GET_ALL, new GiftCertificateMapper());
    }

    @Override
    public boolean deleteCertificate(GiftCertificate certificate) {
        return jdbcTemplate.update(SQL_DELETE_CERTIFICATE, certificate.getId()) > 0;
    }

    @Override
    public boolean updateCertificate(GiftCertificate certificate) {
        return jdbcTemplate.update(SQL_UPDATE_CERTIFICATE, certificate.getName(),
                certificate.getDescription(), certificate.getPrice(), certificate.getDuration(),
                certificate.getId()) > 0;
    }

    @Override
    public boolean createCertificate(GiftCertificate certificate) {
        return jdbcTemplate.update(SQL_INSERT_CERTIFICATE,
                certificate.getName(), certificate.getDescription(), certificate.getPrice(), certificate.getDuration()) > 0;
    }
}
