package com.epam.esm.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@ComponentScan("com.epam.esm")
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
public class AppConfig {

    private final Environment environment;
    private final ApplicationContext context;

    private static final String PROD_URL = "db.url";
    private static final String DEV_URL = "db.dev.url";
    private static final String LOGIN = "db.login";
    private static final String DRIVER = "db.driver";
    private static final String PASSWORD = "db.password";
    private static final String SIZE = "db.size";

    private static final String TEST_DRIVER = "db.test.driver";
    private static final String TEST_URL = "db.test.url";


    public AppConfig(Environment environment, ApplicationContext context) {
        this.environment = environment;
        this.context = context;
    }

    @Bean(name = "DataSource")
    @Profile({"test", "default"})
    public DataSource test() {
        return dataSource(TEST_URL, "", "", TEST_DRIVER);
    }

    @Bean(name = "DataSource")
    @Profile({"dev"})
    public DataSource dev() {
        return dataSource(DEV_URL, LOGIN, PASSWORD, DRIVER);
    }

    @Bean(name = "DataSource")
    @Profile({"prod"})
    public DataSource prod() {
        return dataSource(PROD_URL, LOGIN, PASSWORD, DRIVER);
    }

    private DataSource dataSource(String url, String login, String password, String driver) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(environment.getProperty(url));
        dataSource.setUsername(environment.getProperty(login));
        dataSource.setPassword(environment.getProperty(password));
        dataSource.setDriverClassName(environment.getProperty(driver));
        int size = Integer.parseInt(Objects.requireNonNull(environment.getProperty(SIZE)));
        dataSource.setInitialSize(size);
        return dataSource;
    }

    @Bean(name = "JdbcTemplate")
    JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(context.getBean(DataSource.class));
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(context.getBean(DataSource.class));
        return manager;
    }


}