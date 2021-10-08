package com.epam.esm.dao;

import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {

    private static final Logger LOGGER = LogManager.getLogger();

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> mapper;

    private final static String SQL_FIND_TAG = "select id as tag_id, name as tag_name from tag where id = ?";
    private final static String SQL_DELETE_TAG = "delete from tag where id = ?";
    private final static String SQL_GET_ALL = "select id as tag_id, name as tag_name from tag";
    private final static String SQL_INSERT_TAG = "insert into tag(name) values(?)";


    public TagDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public Tag getTagById(Long id) {
        Tag tag;
        try {
            tag = jdbcTemplate.queryForObject(SQL_FIND_TAG, new Object[]{id}, mapper);
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TagNotFoundException("Tag not found ", id);
        }
        return tag;
    }

    @Override
    public List<Tag> getAllTags() {

        return jdbcTemplate.query(SQL_GET_ALL, mapper);
    }

    @Override
    public void deleteTagById(Long id) {
        jdbcTemplate.update(SQL_DELETE_TAG, id);
    }

    @Override
    public Tag createTag(Tag tag) {
        jdbcTemplate.update(SQL_INSERT_TAG, tag.getName());
        return tag;
    }

}
