package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService extends AbstractService<Tag>{


    private final TagDao dao;
    private final static int DEFAULT_LIMIT = 20;
    private final static int DEFAULT_OFFSET = 0;

    public TagService(TagDao dao) {
        super(dao);
        this.dao = dao;
    }

    public Tag getMostUsedTagOfUserWithHighestOrdersCost() {
        return dao.getMostUsedTagOfUserWithHighestOrdersCost();
    }

    @Override
    public int getDefaultOffset() {
        return DEFAULT_OFFSET;
    }

    @Override
    public int getDefaultLimit() {
        return DEFAULT_LIMIT;
    }
}
