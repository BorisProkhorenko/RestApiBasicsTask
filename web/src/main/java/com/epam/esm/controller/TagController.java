package com.epam.esm.controller;


import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * This Controller provides public API for operations with {@link Tag} entity.
 * Uses {@link TagService} to access Data Base through business-logic layer.
 *
 * @author Boris Prokhorenko
 * @see Tag
 * @see TagService
 */
@RestController
@RequestMapping(value = "/tags")
public class TagController {

    private final TagService service;

    public TagController(TagService service) {
        this.service = service;
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
    @GetMapping(value = "/")
    public List<Tag> getAllTags() {
        return service.getAllTags();
    }

    /**
     * Method allows creating {@link Tag} in DB by its name
     *
     * @param name - name of tag which will be created
     * @return {@link Tag} object, which you created
     */
    @PostMapping(value = "/{name}")
    public Tag createTag(@PathVariable String name) {
        Tag tag = new Tag(name);
        return service.createTag(tag);
    }

    /**
     * Method allows deleting {@link Tag} from DB by its id
     *
     * @param id - primary key to search {@link Tag} entity object in DB
     */
    @DeleteMapping(value = "/{id}")
    public @ResponseBody
    HttpStatus deleteTag(@PathVariable Long id) {
        service.deleteTag(id);
        return HttpStatus.OK;
    }

}
