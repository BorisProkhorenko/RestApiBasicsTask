package com.epam.esm;


import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;



@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static final String RESOURCE_NAME = "i18n";

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename(RESOURCE_NAME);
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }

    @Bean
    public Jdk8Module jdk8Module(){
        return new Jdk8Module();
    }
}
