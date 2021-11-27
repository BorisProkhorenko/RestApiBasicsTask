package com.epam.esm.service;

import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class TagService extends AbstractService<Tag> {


    private final TagRepository repository;

    public TagService(TagRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Tag getMostUsedTagOfUserWithHighestOrdersCost() {
        Page<Tag> page = repository.getPageTagOfUserWithHighestOrdersCost(
                PageRequest.of(0, 1));
        if(!page.hasContent()){
            throw new TagNotFoundException("No orders found");
        }
        return page.getContent().get(0);
    }

}
