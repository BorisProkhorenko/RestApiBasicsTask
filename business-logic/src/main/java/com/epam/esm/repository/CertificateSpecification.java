package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Set;

public class CertificateSpecification implements Specification<Certificate> {

    private final SearchCriteria criteria;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";


    public CertificateSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Certificate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equals(SearchCriteria.Operation.TAGS)) {
          return toTagsPredicate(root, builder);
        }

        if (criteria.getOperation().equals(SearchCriteria.Operation.PART)) {
          return toPartPredicate(root, builder);
        }
        return null;
    }

    private Predicate toTagsPredicate(Root<Certificate> root, CriteriaBuilder builder) {
        SearchCriteria.Operation operation = SearchCriteria.Operation.TAGS;
        String tagsFieldName = operation.name()
                .toLowerCase();
        Expression<Set<Tag>> tags = root.get(tagsFieldName);
        Predicate predicate = builder.conjunction();
        Set<Tag> tagIdSet = (Set<Tag>) criteria.getValue();
        if (!tagIdSet.isEmpty()) {
            for (Tag t : tagIdSet) {
                predicate = builder.and(predicate, builder.isMember(t, tags));
            }
        }
        return predicate;
    }

    private Predicate toPartPredicate(Root<Certificate> root, CriteriaBuilder builder) {
        String part = (String) criteria.getValue();
        Predicate namePredicate = builder.like(root.get(NAME), "%" + part + "%");
        Predicate descriptionPredicate = builder.like(root.get(DESCRIPTION), "%" + part + "%");
        return builder.or(namePredicate, descriptionPredicate);
    }
}
