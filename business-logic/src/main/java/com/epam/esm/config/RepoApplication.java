package com.epam.esm.config;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.*;


@SpringBootApplication(exclude = { //
        DataSourceAutoConfiguration.class, //
        DataSourceTransactionManagerAutoConfiguration.class, //
        HibernateJpaAutoConfiguration.class})
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
public class RepoApplication{

    @Autowired
    private Environment env;


    public static void main(String[] args) {
        SpringApplication.run(RepoApplication.class, args);
    }

    private static final String DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
    private static final String URL = "spring.datasource.url";
    private static final String USERNAME = "spring.datasource.username";
    private static final String PASSWORD = "spring.datasource.password";
    private static final String ENTITY_PACK = "com.epam.esm.model";
    private static final String DIALECT = "hibernate.dialect";
    private static final String DIALECT_PROPERTY = "spring.jpa.properties.hibernate.dialect";
    private static final String SHOW = "hibernate.show_sql";
    private static final String SHOW_PROPERTY = "spring.jpa.show-sql";
    private static final String CURRENT_SESSION_CONTEXT_CLASS = "current_session_context_class";
    private static final String CURRENT_SESSION_CONTEXT_CLASS_PROPERTY =
            "spring.jpa.properties.hibernate.current_session_context_class";

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty(DRIVER_CLASS_NAME)));
        dataSource.setUrl(env.getProperty(URL));
        dataSource.setUsername(env.getProperty(USERNAME));
        dataSource.setPassword(env.getProperty(PASSWORD));
        return dataSource;
    }


    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) throws Exception {
        Properties properties = new Properties();
        properties.put(DIALECT, env.getProperty(DIALECT_PROPERTY));
        properties.put(SHOW, env.getProperty(SHOW_PROPERTY));
        properties.put(CURRENT_SESSION_CONTEXT_CLASS, //
                env.getProperty(CURRENT_SESSION_CONTEXT_CLASS_PROPERTY));
        //properties.put("hibernate.hbm2ddl.auto","update");
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setPackagesToScan(ENTITY_PACK);
        factoryBean.setDataSource(dataSource);
        factoryBean.setHibernateProperties(properties);
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

}
