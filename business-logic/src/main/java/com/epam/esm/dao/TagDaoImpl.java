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
    private final static String SQL_FIND_TAG_BY_NAME = "select id as tag_id, name as tag_name from tag where name = ?";
    private final static String SQL_DELETE_TAG = "delete from tag where id = ?";
    private final static String SQL_GET_ALL = "select id as tag_id, name as tag_name from tag";
    private final static String SQL_INSERT_TAG = "insert into tag(name) values(?)";


    public TagDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public Tag getTagById(Long id) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_TAG, new Object[]{id}, mapper);
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TagNotFoundException(id);
        }

    }

    @Override
    public Tag getTagByName(String name) {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_TAG_BY_NAME, new Object[]{name}, mapper);
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage(), e);
            throw new TagNotFoundException(name);
        }

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
        try {
            getTagByName(tag.getName());
        } catch (TagNotFoundException ex) {
            jdbcTemplate.update(SQL_INSERT_TAG, tag.getName());
        }
        return tag;
    }

}
