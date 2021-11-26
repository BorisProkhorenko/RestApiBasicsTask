package com.epam.esm.service;

import com.epam.esm.model.Identifiable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.Optional;

public abstract class AbstractService<T extends Identifiable> {

    private final PagingAndSortingRepository<T, Long> repository;

    protected AbstractService(PagingAndSortingRepository<T, Long> repository) {
        this.repository = repository;
    }

    public Optional<T> findById(Long id) {
        return repository.findById(id);
    }

    public Page<T> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }


    public void delete(T identifiable) {
        repository.delete(identifiable);
    }

    public T create(T identifiable) {
        return repository.save(identifiable);
    }


}
