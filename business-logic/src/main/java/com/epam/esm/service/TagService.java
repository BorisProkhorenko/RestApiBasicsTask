package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {


    private final TagDao dao;
    private final static int DEFAULT_LIMIT = 20;
    private final static int DEFAULT_OFFSET = 0;

    public TagService(TagDao dao) {
        this.dao = dao;
    }


    public Tag getTagById(Long id) {
        return dao.getTagById(id);
    }

    public List<Tag> getAllTags(Optional<Integer> limit, Optional<Integer> offset) {

        int start = offset.orElse(DEFAULT_OFFSET);
        int lim = limit.orElse(DEFAULT_LIMIT);
        if (start < 0) {
            start = DEFAULT_OFFSET;
        }
        if (lim <= 0) {
            lim = DEFAULT_LIMIT;
        }

        return dao.getAllTags(start, lim);
    }

    public void deleteTag(Long id) {
        Tag tag = new Tag();
        tag.setId(id);
        dao.deleteTag(tag);
    }

    public Tag createTag(Tag tag) {
        return dao.createTag(tag);
    }

    public Tag getMostUsedTagOfUserWithHighestOrdersCost() {
        return dao.getMostUsedTagOfUserWithHighestOrdersCost();
    }

}
