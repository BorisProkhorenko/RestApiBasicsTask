package com.epam.esm.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/tags")
public class TagController {

    private final TagService service;

    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public Tag getTagById(@PathVariable Long id) {
        return service.getTagById(id);
    }

    @GetMapping(value = "/")
    public List<Tag> getAllTags() {
        return service.getAllTags();
    }

    @PostMapping(value = "/{name}")
    public Tag createTag(@PathVariable String name) {
        Tag tag = new Tag(name);
        return service.createTag(tag);
    }

    @DeleteMapping(value = "/{id}")
    public @ResponseBody
    HttpStatus deleteTag(@PathVariable Long id) {
         service.deleteTag(id);
        return HttpStatus.OK;
    }

}
