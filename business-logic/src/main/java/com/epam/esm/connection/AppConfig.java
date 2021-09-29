package com.epam.esm.connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;


import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@ComponentScan("com.epam.esm")
@PropertySource("classpath:db.properties")
public class AppConfig {

   private final Environment environment;


    public AppConfig(Environment environment) {
        this.environment = environment;
    }

    private final String URL = "url";
    private final String LOGIN = "login";
    private final String DRIVER = "driver";
    private final String PASSWORD = "password";
    private final String SIZE = "size";

    @Bean
    DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(environment.getProperty(URL));
        dataSource.setUsername(environment.getProperty(LOGIN));
        dataSource.setPassword(environment.getProperty(PASSWORD));
        dataSource.setDriverClassName(environment.getProperty(DRIVER));
        int size = Integer.parseInt(Objects.requireNonNull(environment.getProperty(SIZE)));
        dataSource.setInitialSize(size);
        return dataSource;
    }
}