package com.jahia.takehome.service;

import com.jahia.takehome.entity.PendingItem;
import com.jahia.takehome.repository.PendingItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PendingItemService {

    private PendingItemRepository pendingItemRepository;
    private int PAGE_SIZE = 100;

    public PendingItemService(PendingItemRepository pendingItemRepository) {
        this.pendingItemRepository = pendingItemRepository;
    }

    public void savePendingItem(PendingItem pendingItem) {
        pendingItemRepository.save(pendingItem);
    }

    public void updatePendingItem(PendingItem pendingItem) {
        Optional<PendingItem> itemFromDb = pendingItemRepository.findById(pendingItem.getId());
        itemFromDb.ifPresentOrElse(item -> {
            item.setName(pendingItem.getName());
            savePendingItem(item);
        }, () -> { throw new IllegalArgumentException("item to be updated not found"); });
    }

    public void deletePendingItem(UUID id) {
        Optional<PendingItem> itemOption = getPendingItem(id);
        itemOption.ifPresent(item -> pendingItemRepository.delete(item));
    }

    public Optional<PendingItem> getPendingItem(UUID id) {
        return pendingItemRepository.findById(id);
    }

    public List<PendingItem> getPendingItems(Integer page, String search) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<PendingItem> itemPage;
        if (!StringUtils.hasText(search)) {
            itemPage = pendingItemRepository.findAll(pageable);
        } else {
            itemPage = pendingItemRepository.getPendingItemsByNameContains(pageable, search);
        }

        return itemPage.stream().sorted(Comparator.comparing(PendingItem::getCreated)).collect(Collectors.toList());
    }

    public List<PendingItem> getPendingItemsWithoutPaging(String search) {
        List<PendingItem> items;
        if (!StringUtils.hasText(search)) {
            items = pendingItemRepository.findAll();
        } else {
            items = pendingItemRepository.findByNameLike(search);
        }

        return items.stream().sorted(Comparator.comparing(PendingItem::getCreated)).collect(Collectors.toList());
    }
}
