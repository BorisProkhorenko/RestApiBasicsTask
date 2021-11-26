package com.epam.esm.repository;

import com.epam.esm.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;



public interface TagRepository extends PagingAndSortingRepository<Tag,Long> {


    @Query(" select tag FROM User u join u.orders o" +
            " join o.snapshots s join s.certificate c join c.tags tag group by tag.id order by" +
            " sum(o.cost) desc ")
    Page<Tag> getPageTagOfUserWithHighestOrdersCost(Pageable pageable);
}
