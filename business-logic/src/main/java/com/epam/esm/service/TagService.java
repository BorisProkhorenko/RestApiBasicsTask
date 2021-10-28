package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {


    private final TagDao dao;

    public TagService(TagDao dao) {
        this.dao = dao;
    }


    public Tag getTagById(Long id) {
        return dao.getTagById(id);
    }

    public List<Tag> getAllTags() {

        return dao.getAllTags();
    }

    public void deleteTag(Long id) {
        Tag tag = new Tag();
        tag.setId(id);
        dao.deleteTag(tag);
    }

    public Tag createTag(Tag tag) {
        return dao.createTag(tag);
    }

    public Tag getMostUsedTagOfUserWithHighestOrdersCost(){
        return dao.getMostUsedTagOfUserWithHighestOrdersCost();
    }

}
