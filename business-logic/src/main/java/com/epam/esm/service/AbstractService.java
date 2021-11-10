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

    public List<T> getAll(Optional<Integer> page, Optional<Integer> size) {
        int limit = getLimit(size);
        int start = getStart(page, limit);
        return dao.getAll(start, limit);
    }


    protected int getStart(Optional<Integer> page, int limit) {
        int start = page.orElse(getDefaultOffset());
        start--;
        if (start < 0) {
            start = getDefaultOffset();
        }
        return start * limit;
    }

    protected int getLimit(Optional<Integer> size) {
        int limit = size.orElse(getDefaultLimit());
        if (limit <= 0) {
            limit = getDefaultOffset();
        }
        return limit;
    }

    public void delete(T identifiable) {
        dao.delete(identifiable);
    }

    public T create(T identifiable) {
        return dao.create(identifiable);
    }

    public int getPagesCount(Optional<Integer> optionalSize) {
        long count = dao.getCount();
        int size = optionalSize.orElse(getDefaultLimit());
        return count % size == 0 ? (int) count / size : (int) count / size + 1;
    }

    public abstract int getDefaultOffset();

    public abstract int getDefaultLimit();

}
