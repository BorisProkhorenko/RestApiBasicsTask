package com.epam.esm.dao;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;


import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface CertificateDao extends Dao<Certificate>{

    Certificate update(Certificate certificate);

    List<Certificate> getAll(Set<Tag> tagIdSet, Optional<String> part,
                             Optional<String> nameSort, Optional<String> dateSort,
                             int start, int limit);



}
