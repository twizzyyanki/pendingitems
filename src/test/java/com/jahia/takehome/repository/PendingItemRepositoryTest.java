package com.jahia.takehome.repository;

import com.jahia.takehome.entity.PendingItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PendingItemRepositoryTest {

    @Autowired
    private PendingItemRepository pendingItemRepository;

    @BeforeEach
    public void loadDb() {
        PendingItem item1 = new PendingItem();
        item1.setName("item1");
        item1.setCreated(LocalDateTime.now());
        pendingItemRepository.save(item1);

        PendingItem item2 = new PendingItem();
        item2.setName("item2");
        item2.setCreated(LocalDateTime.now());
        pendingItemRepository.save(item2);
    }

    @Test
    public void testFindAll() {
        List<PendingItem> items = pendingItemRepository.findAll();
        assertEquals(2, items.size());
    }

    @Test
    public void testPendingItemsByNameContains() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<PendingItem> items = pendingItemRepository.getPendingItemsByNameContains(pageable, "item");
        assertEquals(1, items.getSize());

        pageable = PageRequest.of(1, 100);
        items = pendingItemRepository.getPendingItemsByNameContains(pageable, "item");
        assertEquals(100, items.getSize());
    }
}
