package com.epam.esm.service;

import com.epam.esm.config.RepoApplication;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.OrderNotFoundException;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static javafx.scene.input.KeyCode.H;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RepoApplication.class)
@ActiveProfiles("test")
public class UserServiceTest {

    private static UserService service;
    private final static long MOCK_ID = 1L;
    private final static long OTHER_MOCK_ID = 2L;

    @BeforeAll
    public static void init() {
        UserDao mockUserDao = Mockito.mock(UserDao.class);
        when(mockUserDao.getById(anyLong()))
                .thenReturn(new User(MOCK_ID));
        when(mockUserDao.create(any()))
                .thenReturn(new User(MOCK_ID));
        when(mockUserDao.getAll(0,5))
                .thenReturn(new ArrayList<>());
        OrderDao mockOrderDao = Mockito.mock(OrderDao.class);
        when(mockOrderDao.getById(MOCK_ID))
                .thenReturn(new Order(MOCK_ID,new User(MOCK_ID), new ArrayList<>()));
        when(mockOrderDao.getById(OTHER_MOCK_ID))
                .thenReturn(new Order(OTHER_MOCK_ID,new User(OTHER_MOCK_ID), new ArrayList<>()));

        when(mockOrderDao.create(any()))
                .thenReturn(new Order(MOCK_ID,new User(MOCK_ID), new ArrayList<>()));
        when(mockOrderDao.getAll(0,5))
                .thenReturn(new ArrayList<>());
        service = new UserService(mockUserDao,mockOrderDao);
    }



    @Test
    public void testGetAllUsers() {
        //when
        List<User> users = service.getAll(Optional.of(10),Optional.of(0));
        //then
        Assertions.assertNotNull(users);
    }

    @Test
    public void testGetUserById() {
        //when
        User user = service.getById(MOCK_ID);
        //then
        Assertions.assertEquals(user.getId(), MOCK_ID);
    }

    @Test
    public void testCreateUser() {
        //given
        User mockUser = new User(MOCK_ID);
        //when
        User user = service.create(mockUser);
        //then
        Assertions.assertEquals(user.getId(), mockUser.getId());
    }

    @Test
    public void testGetOrderByUserAndIdOk() {
        //given
        User mockUser = new User(MOCK_ID);
        Order mockOrder = new Order(MOCK_ID,mockUser, new ArrayList<>());
        //when
        Order order = service.getOrderByUserAndId(mockUser.getId(),mockOrder.getId());
        //then
        Assertions.assertEquals(order.getId(), mockOrder.getId());
    }

    @Test
    public void testGetOrderByUserAndIdException() {
        //given
        User mockUser = new User(MOCK_ID);
        Order mockOrder = new Order(OTHER_MOCK_ID);
        //then
        Assertions.assertThrows(OrderNotFoundException.class, () ->
                        service.getOrderByUserAndId(mockUser.getId(),mockOrder.getId()));

    }
}
