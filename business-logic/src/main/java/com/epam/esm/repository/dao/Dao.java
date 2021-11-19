package com.epam.esm.repository.dao;


import com.epam.esm.model.Identifiable;

import java.util.List;

public interface Dao<T extends Identifiable> {
    T getById(Long id);

    List<T> getAll(int start, int limit);

    void delete(T identifiable);

    T create(T identifiable);

    long getCount();
}
