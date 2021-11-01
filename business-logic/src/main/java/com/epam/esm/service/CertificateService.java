package com.epam.esm.service;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.model.Certificate;

import com.epam.esm.model.Tag;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class CertificateService extends AbstractService<Certificate>{

    private final CertificateDao dao;
    private final static int DEFAULT_LIMIT = 5;
    private final static int DEFAULT_OFFSET = 0;

    public CertificateService(CertificateDao dao) {
        super(dao);
        this.dao = dao;
    }


    public List<Certificate> getAll(Set<Long> tagIdSet, Optional<String> part,
                                    Optional<String> nameSort, Optional<String> dateSort,
                                    Optional<Integer> limit, Optional<Integer> offset) {

        int start = getStart(offset);
        int lim = getLimit(limit);
        Set<Tag> tags = tagIdSet.stream()
                .map(Tag::new)
                .collect(Collectors.toSet());
        return dao.getAll(tags, part, nameSort, dateSort, start, lim);
    }


    public Certificate update(Certificate certificate) {
        return dao.update(certificate);
    }

    @Override
    public int getDefaultOffset() {
        return DEFAULT_OFFSET;
    }

    @Override
    public int getDefaultLimit() {
        return DEFAULT_LIMIT;
    }


}
