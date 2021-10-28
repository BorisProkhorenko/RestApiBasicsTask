package com.epam.esm.controller;


import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * This Controller provides public API for operations with {@link Tag} entity.
 * Uses {@link TagService} to access Data Base through business-logic layer.
 * Uses {@link ObjectMapper} to map objects from JSON
 *
 * @author Boris Prokhorenko
 * @see Tag
 * @see TagService
 * @see ObjectMapper
 */
@RestController
@RequestMapping(value = "/tags")
public class TagController {

    private final TagService service;
    private final ObjectMapper objectMapper;

    public TagController(TagService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    /**
     * Method allows getting {@link Tag} from DB by its id
     *
     * @param id - primary key to search {@link Tag} entity object in DB
     * @return {@link Tag} entity object from DB
     */
    @GetMapping(value = "/{id}")
    public Tag getTagById(@PathVariable Long id) {
        return service.getTagById(id);
    }

    /**
     * Method allows getting all {@link Tag} entity objects from DB
     *
     * @return {@link List} of {@link Tag} entity objects from DB
     */
    @GetMapping
    public List<Tag> getAllTags() {
        return service.getAllTags();
    }

    /**
     * Method allows creating {@link Tag} in DB by its name
     *
     * @param json - tag object to map from request body
     * @return {@link Tag} object, which you created
     */
    @PostMapping(consumes = "application/json")
    public Tag createTag(@RequestBody String json) {
        try {
            Tag tag = objectMapper.readValue(json, Tag.class);
            return service.createTag(tag);
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(e.getMessage());
        }

    }

    /**
     * Method allows deleting {@link Tag} from DB by its id
     *
     * @param id - primary key to search {@link Tag} entity object in DB
     */
    @DeleteMapping(value = "/{id}")
    public void deleteTag(@PathVariable Long id) {
        service.deleteTag(id);
    }


    /**
     * Method allows getting all {@link Tag} entity objects from DB
     *
     * @return {@link List} of {@link Tag} entity objects from DB
     */
    @GetMapping(value = "/highest-order")
    public Tag getMostUsedTagOfUserWithHighestOrdersCost() {
        return service.getMostUsedTagOfUserWithHighestOrdersCost();
    }
}
