package com.epam.esm.service;

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
        return page.getContent().get(0);
    }

}
