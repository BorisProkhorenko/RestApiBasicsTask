package com.epam.esm.dao;

import com.epam.esm.model.Tag;
import com.epam.esm.model.User;

import java.util.List;

public interface TagDao extends Dao<Tag> {



    Tag getTagByName(String name);

    Tag getMostUsedTagOfUserWithHighestOrdersCost();
}
