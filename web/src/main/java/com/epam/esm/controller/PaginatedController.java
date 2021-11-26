package com.epam.esm.controller;

import com.epam.esm.dto.Dto;
import com.epam.esm.model.Identifiable;
import com.epam.esm.service.AbstractService;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public abstract class PaginatedController<T extends PaginatedController, V extends Dto, I extends Identifiable> {

    private final AbstractService<I> service;
    private final static String FIRST = "first";
    private final static String PREV = "prev";
    private final static String NEXT = "next";
    private final static String LAST = "last";
    private final static String REPLACE_REGEX = "\\{.*}";
    private final static String EMPTY = "";
    private final static int FIRST_PAGE = 1;

    public PaginatedController(AbstractService service) {
        this.service = service;
    }

    protected List<Link> buildPagination(Page<I> page, Class<T> pageableClass,
                                         Optional<String> tags, Optional<String> part, String sort) {
        List<Link> links = new ArrayList<>();
        int lastPage = page.getTotalPages();
        int size = page.getSize();
        int current = page.getNumber();
        current = validatePage(current, lastPage);
        buildFirstPrev(current, size, pageableClass, tags, part, sort, links);
        buildNextLast(current, lastPage, size, pageableClass, tags, part, sort, links);
        return getValidLinks(links);
    }

    private List<Link> getValidLinks(List<Link> links) {
        return links
                .stream()
                .map(this::deleteEmptyParams)
                .collect(Collectors.toList());
    }

    private Link deleteEmptyParams(Link link) {
        String linkString = link.getHref();
        String rel = link.getRel().value();
        String valid = linkString.replaceAll(REPLACE_REGEX, EMPTY);
        return Link.of(valid).withRel(rel);
    }

    private int validatePage(int currentPage, int lastPage) {
        if (currentPage > lastPage) {
            currentPage = lastPage;
        }
        if (currentPage < FIRST_PAGE) {
            currentPage = FIRST_PAGE;
        }
        return currentPage;
    }

    private void buildFirstPrev(int currentPage, int size, Class<T> pageableClass,
                                Optional<String> tags, Optional<String> part,
                                String sort, List<Link> links) {
        if (currentPage > FIRST_PAGE) {
            Link first = linkTo(methodOn(pageableClass).getAll(FIRST_PAGE, size, tags,
                    part, sort)).withRel(FIRST);
            links.add(first);
            Link previous = linkTo(methodOn(pageableClass).getAll(currentPage - 1, size,
                    tags, part, sort)).withRel(PREV);
            links.add(previous);
        }
    }

    private void buildNextLast(int currentPage, int lastPage, int size, Class<T> pageableClass,
                               Optional<String> tags, Optional<String> part,
                               String sort, List<Link> links) {
        if (currentPage < lastPage) {
            Link next = linkTo(methodOn(pageableClass).getAll(currentPage + 1, size,
                    tags, part, sort)).withRel(NEXT);
            links.add(next);
            Link last = linkTo(methodOn(pageableClass).getAll(lastPage, size,
                    tags, part, sort)).withRel(LAST);
            links.add(last);
        }
    }

    protected List<Link> buildPagination(Page<I> page, Class<T> pageableClass) {
        return buildPagination(page, pageableClass, Optional.empty(), Optional.empty(),
                EMPTY);
    }

    public abstract CollectionModel<V> getAll(int page, int size);


    public abstract CollectionModel<V> getAll(int page, int size,
                                              Optional<String> tags, Optional<String> part,
                                              String sort);


}
