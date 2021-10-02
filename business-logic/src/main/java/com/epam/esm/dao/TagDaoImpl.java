package com.epam.esm.dao;

import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> mapper;

    private final String SQL_FIND_TAG = "select id as tag_id, name as tag_name from tag where id = ?";
    private final String SQL_DELETE_TAG = "delete from tag where id = ?";
    private final String SQL_GET_ALL = "select id as tag_id, name as tag_name from tag";
    private final String SQL_INSERT_TAG = "insert into tag(name) values(?)";


    public TagDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public Tag getTagById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_TAG, new Object[]{id}, mapper);
    }

    @Override
    public List<Tag> getAllTags() {
        return jdbcTemplate.query(SQL_GET_ALL, mapper);
    }

    @Override
    public void deleteTag(Tag tag) {
        jdbcTemplate.update(SQL_DELETE_TAG, tag.getId());
    }

    @Override
    public Tag createTag(Tag tag) {
        jdbcTemplate.update(SQL_INSERT_TAG, tag.getName());
        return tag;
    }

}
