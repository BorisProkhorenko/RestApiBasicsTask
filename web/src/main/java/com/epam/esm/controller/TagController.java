package com.epam.esm.controller;


import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.TagDtoMapper;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.service.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


/**
 * This Controller provides public API for operations with {@link Tag} entity.
 * Uses {@link TagService} to access Data Base through business-logic layer.
 * Uses {@link ObjectMapper} to map objects from JSON
 * Uses {@link TagDtoMapper} to map objects from Entity to Dto
 * Uses {@link PaginatedController} for pagination
 *
 * @author Boris Prokhorenko
 * @see Tag
 * @see TagDto
 * @see TagDtoMapper
 * @see TagService
 * @see ObjectMapper
 * @see PaginatedController
 */
@RestController
@RequestMapping(value = "/tags")
public class TagController extends PaginatedController<TagController, TagDto, Tag> {

    private final TagService service;
    private final ObjectMapper objectMapper;
    private final TagDtoMapper dtoMapper;

    private final static String TAGS = "tags";
    private final static String MOST_USED_TAG = "most_used_tag_of_user_with_highest_orders_cost";
    private final static String MOST_USED_TAG_URL = "highest-order";

    public TagController(TagService service, ObjectMapper objectMapper, TagDtoMapper dtoMapper) {
        super(service);
        this.service = service;
        this.objectMapper = objectMapper;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Method allows getting {@link Tag} from DB by its id
     *
     * @param id - primary key to search {@link Tag} entity object in DB
     * @return {@link TagDto} of entity object from DB
     */
    @GetMapping(value = "/{id}")
    public TagDto getTagById(@PathVariable Long id) {
        TagDto tag = dtoMapper.toDto(service.getById(id));
        buildTagLinks(tag);
        return tag;
    }

    /**
     * Method allows getting all {@link Tag} entity objects from DB
     *
     * @param page - page of displayed dto objects
     * @param size - count of displayed dto objects
     * @return {@link CollectionModel} of {@link TagDto} of entity objects from DB
     */
    @Override
    @GetMapping(produces = {"application/hal+json"})
    public CollectionModel<TagDto> getAll(@RequestParam(name = "page") Optional<Integer> page,
                                          @RequestParam(name = "size") Optional<Integer> size) {

        List<TagDto> tags = service.getAll(page, size)
                .stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());
        buildTagCollectionLinks(tags);
        List<Link> links = buildPagination(page, size, TagController.class);
        Link mostUsedTag = linkTo(TagController.class).slash(MOST_USED_TAG_URL).withRel(MOST_USED_TAG);
        links.add(mostUsedTag);
        Link selfLink = linkTo(TagController.class).withSelfRel();
        links.add(selfLink);
        return CollectionModel.of(tags, links);

    }

    @Override
    public CollectionModel<TagDto> getAll(@RequestParam(name = "page") Optional<Integer> page,
                                          @RequestParam(name = "size") Optional<Integer> size,
                                          @RequestParam(name = "filter_by_tags") Optional<String> tags,
                                          @RequestParam(name = "filter_by_part") Optional<String> part,
                                          @RequestParam(name = "sort_by_name") Optional<String> name,
                                          @RequestParam(name = "sort_by_date") Optional<String> date) {
        return getAll(page, size);
    }

    /**
     * Method allows creating {@link Tag} in DB by its name
     *
     * @param json - tag object to map from request body
     * @return {@link TagDto} of object, which you created
     */
    @PostMapping(consumes = "application/json")
    @Secured("ROLE_ADMIN")
    public TagDto createTag(@RequestBody String json) {
        try {
            Tag tag = objectMapper.readValue(json, Tag.class);
            TagDto tagDto = dtoMapper.toDto(service.create(tag));
            buildTagLinks(tagDto);
            return tagDto;
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
    @Secured("ROLE_ADMIN")
    public void deleteTag(@PathVariable Long id) {
        service.delete(new Tag(id));
    }


    /**
     * Method allows getting the most widely used {@link Tag} of a {@link User} with the highest cost of all orders
     *
     * @return {@link TagDto} of entity object from DB
     */
    @GetMapping(value = "/highest-order")
    public TagDto getMostUsedTagOfUserWithHighestOrdersCost() {
        TagDto tag = dtoMapper.toDto(service.getMostUsedTagOfUserWithHighestOrdersCost());
        buildTagLinks(tag);
        return tag;
    }

    private void buildTagLinks(TagDto tag) {
        Link mostUsedTag = linkTo(TagController.class).slash(MOST_USED_TAG_URL).withRel(MOST_USED_TAG);
        tag.add(mostUsedTag);
        Link allTags = linkTo(TagController.class).withRel(TAGS);
        tag.add(allTags);
        Long id = tag.getId();
        Link selfLink = linkTo(TagController.class).slash(id).withSelfRel();
        tag.add(selfLink);
    }

    /*package-private*/
    static void buildTagCollectionLinks(Iterable<TagDto> tags) {
        for (TagDto tag : tags) {
            Long id = tag.getId();
            Link selfLink = linkTo(TagController.class).slash(id).withSelfRel();
            tag.add(selfLink);
        }
    }


}
