package com.epam.esm.dto;


import com.epam.esm.model.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagDtoMapper {

    public TagDto toDto(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }
}
