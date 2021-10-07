package com.epam.esm.dao;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagDao {

    Tag getTagById(Long id);

    List<Tag> getAllTags();

    void deleteTagById(Long id);

    Tag createTag(Tag tag);
}