package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface CertificateRepository extends PagingAndSortingRepository<Certificate, Long>, JpaSpecificationExecutor<Certificate> {





}
