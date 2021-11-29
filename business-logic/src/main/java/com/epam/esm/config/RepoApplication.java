package com.epam.esm.config;


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
public class RepoApplication {


    public static void main(String[] args) {
        SpringApplication.run(RepoApplication.class, args);
    }


}
