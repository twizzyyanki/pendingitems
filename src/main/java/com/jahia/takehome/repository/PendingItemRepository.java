package com.jahia.takehome.repository;

import com.jahia.takehome.entity.PendingItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PendingItemRepository extends PagingAndSortingRepository<PendingItem, UUID> {

    Page<PendingItem> getPendingItemsByNameContains(Pageable pageable, String name);

    @Query("SELECT p FROM PendingItem p WHERE upper(p.name) LIKE upper(?1)")
    List<PendingItem> findByNameLike(String name);

    List<PendingItem> findAll();
}
