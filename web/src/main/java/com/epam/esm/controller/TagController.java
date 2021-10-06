package com.epam.esm.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class TagController {

    private final TagService service;

    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping(value = "/tags/{id}")
    public @ResponseBody
    Tag getTagById(@PathVariable Long id) {
        return service.getTagById(id);
    }

    @GetMapping(value = "/tags")
    public @ResponseBody
    List<Tag> getAllTags() {
        return service.getAllTags();
    }

    @PostMapping(value = "/tags/{name}")
    public @ResponseBody
    Tag createTag(@PathVariable String name) {
        Tag tag = new Tag(name);
        return service.createTag(tag);
    }

    @DeleteMapping(value = "/tags/{id}")
    public @ResponseBody
    void deleteTag(@PathVariable Long id) {
         service.deleteTag(id);
    }

}
