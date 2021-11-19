package com.epam.esm.service;

import com.epam.esm.config.RepoApplication;
import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RepoApplication.class)
@ActiveProfiles("test")
public class TagServiceTest {


    private static TagService service;
    private final static String MOCK = "mock";

    @BeforeAll
    public static void init() {
        TagDao mockDao = Mockito.mock(TagDao.class);
        when(mockDao.getById(anyLong()))
                .thenReturn(new Tag(MOCK));
        when(mockDao.create(any()))
                .thenReturn(new Tag(MOCK));
        when(mockDao.getAll(0,5))
                .thenReturn(new ArrayList<>());
        service = new TagService(mockDao);
    }


    @Test
    public void testGetAll() {
        //when
        List<Tag> tags = service.getAll(Optional.of(10),Optional.of(0));
        //then
        Assertions.assertNotNull(tags);
    }

    @Test
    public void testGetById() {
        //when
        Tag tag = service.getById(1L);
        //then
        Assertions.assertEquals(tag.getName(), MOCK);
    }

    @Test
    public void testCreate() {
        //given
        Tag mockTag = new Tag(MOCK);
        //when
        Tag tag = service.create(mockTag);
        //then
        Assertions.assertEquals(tag.getName(), MOCK);
    }


}
