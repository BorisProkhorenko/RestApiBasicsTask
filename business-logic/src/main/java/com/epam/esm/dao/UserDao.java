package com.epam.esm.dao;

import com.epam.esm.model.User;

import java.util.List;

public interface UserDao {

    User getUserById(Long id);

    User getUserByUsername(String username);

    List<User> getAllUsers();

    User createUser(User user);


    void deleteUser(User user);

}
