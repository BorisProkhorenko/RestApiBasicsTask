package com.epam.esm.service;

import com.epam.esm.config.RepoApplication;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RepoApplication.class)
@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-test.properties")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Transactional
public class TagServiceTest {
    @Autowired
    private TagService service;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private UserService userService;


    @Test
    public void testCreateAndGetByIdOk() {
        //given
        Tag tag = new Tag("name");
        //when
        service.create(tag);
        Optional<Tag> optionalTag = service.findById(1L);
        //then
        Assertions.assertEquals("name", optionalTag.get().getName());
    }

    @Test
    public void testCreateAndGetAllOk() {
        //given
        Tag tag = new Tag("name");
        //when
        service.create(tag);
        Page<Tag> tags = service.findAll(0, 5);
        //then
        Assertions.assertEquals(1, tags.getContent().size());
    }

    @Test()
    public void testCreateAndDeleteByIdEmpty() {
        //given
        Tag tag = new Tag("name");
        //when
        service.create(tag);
        tag.setId(1L);
        service.delete(tag);
        Optional<Tag> fromDb = service.findById(1L);
        //then
        Assertions.assertFalse(fromDb.isPresent());
    }

    @Test
    public void testGetMostUsedTagOfUserWithHighestOrdersCost() {
        //given
        Tag tag1 = new Tag(1, "1");
        Tag tag2 = new Tag(2, "2");
        Tag tag3 = new Tag(3, "3");
        Set<Tag> tagSet1 = new HashSet<>();
        tagSet1.add(tag1);
        Set<Tag> tagSet2 = new HashSet<>();
        tagSet2.add(tag1);
        tagSet2.add(tag2);
        Set<Tag> tagSet3 = new HashSet<>();
        tagSet2.add(tag3);
        Certificate certificate1 = new Certificate(1, "1", "1", 1D, 1, tagSet1);
        Certificate certificate2 = new Certificate(2, "2", "2", 10D, 1, tagSet2);
        Certificate certificate3 = new Certificate(3, "3", "3", 2D, 1, tagSet3);
        certificateService.create(certificate1);
        certificateService.create(certificate2);
        certificateService.create(certificate3);
        User user1 = new User(1, "1", new HashSet<>());
        user1.setPassword("");
        User user2 = new User(2, "2", new HashSet<>());
        user2.setPassword("");
        Order order1 = new Order(1, user1, Arrays.asList(certificate1, certificate2));
        Order order2 = new Order(2, user1, Arrays.asList(certificate1));
        Order order3 = new Order(3, user2, Arrays.asList(certificate3));
        userService.create(user1);
        userService.create(user2);
        userService.createOrder(order1);
        userService.createOrder(order2);
        userService.createOrder(order3);
        //when
        Tag tag = service.getMostUsedTagOfUserWithHighestOrdersCost();
        //then
        Assertions.assertEquals(tag1, tag);
    }

    @Test
    public void testGetMostUsedTagOfUserWithHighestOrdersCostError() {
        //then
        Assertions.assertThrows(TagNotFoundException.class, () ->
                service.getMostUsedTagOfUserWithHighestOrdersCost());
    }
}


