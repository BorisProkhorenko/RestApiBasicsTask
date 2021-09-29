package com.epam.esm.mapper;

import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapper implements RowMapper<Tag> {

    private static final String ID = "id";
    private static final String NAME = "name";

    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        Tag tag = new Tag();
        tag.setId(resultSet.getLong(ID));
        tag.setName(resultSet.getString(NAME));
        return tag;
    }
}
