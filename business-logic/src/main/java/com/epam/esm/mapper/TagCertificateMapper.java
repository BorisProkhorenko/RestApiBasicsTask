package com.epam.esm.mapper;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;


@Repository
public class TagCertificateMapper implements RowMapper<TagCertificate> {

    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    private static final String ID = "id";
    private static final String TAG_ID = "tag_id";
    private static final String GIFT_CERTIFICATE_ID = "tag_id";

    public TagCertificateMapper(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public TagCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        TagCertificate tagCertificate = new TagCertificate();
        tagCertificate.setId(resultSet.getLong(ID));
        long tagId = resultSet.getLong(TAG_ID);
        Tag tag = tagDao.getTagById(tagId);
        tagCertificate.setTag(tag);
        long giftCertificateId = resultSet.getLong(GIFT_CERTIFICATE_ID);
        GiftCertificate giftCertificate = giftCertificateDao.getCertificateById(giftCertificateId);
        tagCertificate.setCertificate(giftCertificate);

        return tagCertificate;
    }
}
