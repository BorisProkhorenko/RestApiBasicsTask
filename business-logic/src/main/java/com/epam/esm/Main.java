package com.epam.esm;

import com.epam.esm.config.AppConfig;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        GiftCertificateService service = context.getBean(GiftCertificateService.class);
        List<GiftCertificate> certificates = service.getCertificatesWithParams(Optional.of("3"),
                Optional.empty(),Optional.empty(),Optional.empty());

        for (GiftCertificate c:certificates) {
            System.out.println(c);
        }
    }
}
