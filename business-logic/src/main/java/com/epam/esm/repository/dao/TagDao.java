package com.epam.esm.repository.dao;

import com.epam.esm.model.Tag;

public interface TagDao extends Dao<Tag> {



    Tag getTagByName(String name);

    Tag getMostUsedTagOfUserWithHighestOrdersCost();
}
