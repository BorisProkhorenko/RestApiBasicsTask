package com.epam.esm.service;

import com.epam.esm.config.RepoApplication;
import com.epam.esm.exceptions.OrderNotFoundException;
import com.epam.esm.model.Order;
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

import java.util.*;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RepoApplication.class)
@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-test.properties")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService service;

    @Test
    public void testCreateAndGetByIdOk() {
        //given
        User user = new User(1, "name", new HashSet<>());
        user.setPassword("");
        //when
        service.create(user);
        Optional<User> optionalUser = service.findById(1L);
        //then
        Assertions.assertEquals("name", optionalUser.get().getUsername());
    }

    @Test
    public void testCreateAndGetAllOk() {
        //given
        User user = new User(1, "name", new HashSet<>());
        user.setPassword("");
        //when
        service.create(user);
        Page<User> users = service.findAll(0, 5);
        //then
        Assertions.assertEquals(1, users.getContent().size());
    }

    @Test()
    public void testCreateAndDeleteByIdEmpty() {
        //given
        User user = new User(1, "name", new HashSet<>());
        user.setPassword("");
        //when
        service.create(user);
        service.delete(user);
        Optional<User> fromDb = service.findById(1L);
        //then
        Assertions.assertFalse(fromDb.isPresent());
    }

    @Test
    public void testCreateAndGetByUsernameOk() {
        //given
        User user = new User(1, "name", new HashSet<>());
        user.setPassword("");
        //when
        service.create(user);
        User fromDb = service.findUserByUsername("name");
        //then
        Assertions.assertEquals("name", fromDb.getUsername());
    }

    @Test
    public void testCreateOrderAndGetByByUserAndIdOk() {
        //given
        User user = new User(1, "name", new HashSet<>());
        user.setPassword("");
        Order order = new Order(1, user, new ArrayList<>());
        //when
        service.create(user);
        service.createOrder(order);
        Order fromDb = service.getOrderByUserAndId(user.getId(), order.getId());
        //then
        Assertions.assertEquals(order.getId(), fromDb.getId());
    }

    @Test
    public void testCreateOrderAndDeleteAndGetByByUserAndIdError() {
        //given
        User user = new User(1, "name", new HashSet<>());
        user.setPassword("");
        Order order = new Order(1, user, new ArrayList<>());
        service.create(user);
        service.createOrder(order);
        service.deleteOrder(order);
        //then
        Assertions.assertThrows(OrderNotFoundException.class, () ->
                service.getOrderByUserAndId(user.getId(), order.getId()));
    }
}
