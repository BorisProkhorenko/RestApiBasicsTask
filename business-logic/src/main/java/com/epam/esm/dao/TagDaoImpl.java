package com.epam.esm.dao;

import com.epam.esm.mapper.TagMapper;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class TagDaoImpl implements TagDao{

    private final JdbcTemplate jdbcTemplate;

    private final String SQL_FIND_TAG = "select * from tag where id = ?";
    private final String SQL_DELETE_TAG = "delete from tag where id = ?";
    private final String SQL_GET_ALL = "select * from tag";
    private final String SQL_INSERT_TAG = "insert into tag(name) values(?)";

    @Autowired
    public TagDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Tag getTagById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_TAG, new Object[] { id }, new TagMapper());
    }

    @Override
    public List<Tag> getAllTags() {
        return jdbcTemplate.query(SQL_GET_ALL, new TagMapper());
    }

    @Override
    public boolean deleteTag(Tag tag) {
        return jdbcTemplate.update(SQL_DELETE_TAG, tag.getId()) > 0;
    }

    @Override
    public boolean createTag(Tag tag) {
        return jdbcTemplate.update(SQL_INSERT_TAG, tag.getName()) > 0;
    }

}
