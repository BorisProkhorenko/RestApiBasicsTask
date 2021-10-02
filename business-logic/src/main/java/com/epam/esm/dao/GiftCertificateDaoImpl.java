package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {


    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> mapper;

    private final String SQL_FIND_CERTIFICATE = "select gift_certificate_id as gc_id, gc.name as gc_name," +
            "description, price, duration, create_date, last_update_date, tag_id, tag.name as tag_name" +
            " from gift_certificate as gc join tag_gift_certificate as tgc on " +
            "gc.id = tgc.gift_certificate_id join tag on tgc.tag_id = tag.id where gc.id = ?";

    private final String SQL_DELETE_CERTIFICATE = "delete from gift_certificate where id = ?";

    private final String SQL_GET_ALL = "select gift_certificate_id as gc_id, gc.name as gc_name," +
            "description, price, duration, create_date, last_update_date, tag_id, tag.name as tag_name" +
            " from gift_certificate as gc join tag_gift_certificate as tgc on " +
            "gc.id = tgc.gift_certificate_id join tag on tgc.tag_id = tag.id";

    private final String SQL_INSERT_CERTIFICATE = "insert into gift_certificate(name, description," +
            " price, duration) values(?,?,?,?)";

    private final String SQL_UPDATE_CERTIFICATE = "update gift_certificate set name = COALESCE(?,name)," +
            " description = COALESCE(?,description), price  = COALESCE(?,price)," +
            " duration = COALESCE(?,duration) where id = ?";

    private final String SQL_INSERT_TAG_CERTIFICATE = "insert into tag_gift_certificate(tag_id, gift_certificate_id," +
            " values(?,?)";

    private final String SQL_DELETE_TAG_CERTIFICATE = "delete from tag_gift_certificate where tag_id = ?" +
            " and gift_certificate_id = ?";

    private final String SQL_GET_BY_TAG = " where tag_id = ?";

    private final String SQL_FIND_PART = " WHERE gc.name LIKE ? or description LIKE ?";
    private final String SQL_FIND_TAG_PART = " and (gc.name LIKE ? or description LIKE ?)";

    private final String SQL_ORDER_BY_NAME = " order by gc_name";
    private final String SQL_ORDER_BY_DESCRIPTION = " order by description";
    private final String SQL_ORDER_BY_NAME_DESCRIPTION = ", description";
    private final String SQL_DESC = " desc";


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

    @Override
    public void addTag(Tag tag, GiftCertificate certificate) {
        jdbcTemplate.update(SQL_INSERT_TAG_CERTIFICATE,
                tag.getId(), certificate.getId());
    }

    @Override
    public void removeTag(Tag tag, GiftCertificate certificate) {
        jdbcTemplate.update(SQL_DELETE_TAG_CERTIFICATE,
                tag.getId(), certificate.getId());
    }

    @Override
    public List<GiftCertificate> getAllCertificates(Optional<Tag> tag, Optional<String> part,
                                                    Optional<Boolean> nameDesc,
                                                    Optional<Boolean> descriptionDesc) {

        StringBuilder builder = new StringBuilder(SQL_GET_ALL);
        List<Object> params = new ArrayList<>();
        buildFindByTagQuery(builder, params, tag);
        buildFindByPartQuery(builder, params, tag, part);
        buildSortByNameQuery(builder, nameDesc);
        buildSortByDescriptionQuery(builder, nameDesc, descriptionDesc);


        return jdbcTemplate.query(builder.toString(), params.toArray(), mapper);
    }


    private void buildFindByTagQuery(StringBuilder builder, List<Object> params,
                                     Optional<Tag> optionalTag) {
        if (optionalTag.isPresent()) {
            builder.append(SQL_GET_BY_TAG);
            Tag tag = optionalTag.get();
            params.add(tag.getId());
        }

    }

    private void buildFindByPartQuery(StringBuilder builder, List<Object> params,
                                      Optional<Tag> tag, Optional<String> part) {
        if (part.isPresent()) {
            if (tag.isPresent()) {
                builder.append(SQL_FIND_TAG_PART);
            } else {
                builder.append(SQL_FIND_PART);
            }
            String pattern = "%" + part.get() + "%";
            params.add(pattern);
            params.add(pattern);

        }

    }


    private void buildSortByNameQuery(StringBuilder builder, Optional<Boolean> nameDesc) {
        if (nameDesc.isPresent()) {
            builder.append(SQL_ORDER_BY_NAME);
            if (nameDesc.get().equals(true)) {
                builder.append(SQL_DESC);
            }
        }
    }

    private void buildSortByDescriptionQuery(StringBuilder builder, Optional<Boolean> nameDesc,
                                             Optional<Boolean> descriptionDesc) {
        if (descriptionDesc.isPresent()) {
            if (nameDesc.isPresent()) {
                builder.append(SQL_ORDER_BY_NAME_DESCRIPTION);
            } else {
                builder.append(SQL_ORDER_BY_DESCRIPTION);
            }
            if (descriptionDesc.get().equals(true)) {
                builder.append(SQL_DESC);
            }
        }
    }

}
