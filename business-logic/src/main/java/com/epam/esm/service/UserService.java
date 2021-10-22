package com.epam.esm.service;

import com.epam.esm.dao.UserDao;
import com.epam.esm.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao dao;

    public UserService(UserDao dao) {
        this.dao = dao;
    }


    public User getUserById(Long id) {
        return dao.getUserById(id);
    }

    public List<User> getAllUsers() {

        return dao.getAllUsers();
    }
}
