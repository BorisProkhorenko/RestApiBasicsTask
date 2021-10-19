package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TagServiceTest {


    private static TagService service;
    private final static String MOCK = "mock";

    @BeforeAll
    public static void init() {
        TagDao mockDao = Mockito.mock(TagDao.class);
        when(mockDao.getTagById(anyLong()))
                .thenReturn(new Tag(MOCK));
        when(mockDao.createTag(any()))
                .thenReturn(new Tag(MOCK));
        when(mockDao.getAllTags())
                .thenReturn(new ArrayList<>());
        service = new TagService(mockDao);
    }


    @Test
    public void testGetAll() {
        //when
        List<Tag> tags = service.getAllTags();
        //then
        Assertions.assertNotNull(tags);
    }

    @Test
    public void testGetById() {
        //when
        Tag tag = service.getTagById(1L);
        //then
        Assertions.assertEquals(tag.getName(), MOCK);
    }

    @Test
    public void testCreate() {
        //given
        Tag mockTag = new Tag(MOCK);
        //when
        Tag tag = service.createTag(mockTag);
        //then
        Assertions.assertEquals(tag.getName(), MOCK);
    }
}
