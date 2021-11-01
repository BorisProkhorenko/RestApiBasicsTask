package com.epam.esm.service;

import com.epam.esm.dao.Dao;
import com.epam.esm.model.Identifiable;


import java.util.List;
import java.util.Optional;

public abstract class AbstractService<T extends Identifiable> {

    private final Dao<T> dao;

    protected AbstractService(Dao<T> dao) {
        this.dao = dao;
    }

    public T getById(Long id) {
        return dao.getById(id);
    }

    public List<T> getAll(Optional<Integer> limit, Optional<Integer> offset) {
        int start = getStart(offset);
        int lim = getLimit(limit);
        return dao.getAll(start, lim);
    }



    protected int getStart(Optional<Integer> offset){
        int start = offset.orElse(getDefaultOffset());
        if (start < 0) {
            start = getDefaultOffset();
        }
        return start;
    }

    protected int getLimit(Optional<Integer> limit){
        int lim = limit.orElse(getDefaultLimit());
        if (lim <= 0) {
            lim = getDefaultOffset();
        }
        return lim;
    }

    public void delete(T identifiable) {
        dao.delete(identifiable);
    }

    public T create(T tag) {
        return dao.create(tag);
    }

    public abstract int getDefaultOffset();
    public abstract int getDefaultLimit();

}
