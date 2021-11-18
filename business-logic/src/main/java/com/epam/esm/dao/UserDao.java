package com.epam.esm.dao;

import com.epam.esm.model.User;


public interface UserDao extends Dao<User>{

    User getUserByUsername(String username);

}
