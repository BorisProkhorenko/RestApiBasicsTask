package com.epam.esm.config;


import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@ComponentScan("com.epam.esm")
@EnableJpaRepositories(basePackages = "com.epam.esm.repository")
@EntityScan("com.epam.esm.model")
public class RepoApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(RepoApplication.class, args);
    }

    @Autowired
    private UserService service;

    @Override
    public void run(ApplicationArguments args) {

    }
}
