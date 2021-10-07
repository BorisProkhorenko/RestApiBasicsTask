package com.epam.esm;

import com.epam.esm.config.AppConfig;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        GiftCertificateService service = context.getBean(GiftCertificateService.class);
        GiftCertificate certificate = service.getCertificateById(10L);
        System.out.println(certificate);
    }
}
