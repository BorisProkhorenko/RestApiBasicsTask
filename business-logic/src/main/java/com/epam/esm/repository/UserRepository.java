package com.epam.esm.repository;

import com.epam.esm.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(String username);

}
