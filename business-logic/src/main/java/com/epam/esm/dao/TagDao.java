package com.epam.esm.dao;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagDao {

    Tag getTagById(Long id);

    List<Tag> getAllTags();

    boolean deleteTag(Tag tag);

    boolean createTag(Tag tag);
}
