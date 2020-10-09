package com.jahia.takehome.service;

import com.jahia.takehome.entity.PendingItem;
import com.jahia.takehome.repository.PendingItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PendingItemServiceTest {

    private PendingItemRepository pendingItemRepository = mock(PendingItemRepository.class);

    private PendingItemService pendingItemService = new PendingItemService(pendingItemRepository);

    @Test
    public void testSavePendingItem() {
        PendingItem pendingItem = new PendingItem();
        pendingItem.setName("item1");
        pendingItem.setId(UUID.randomUUID());
        pendingItem.setCreated(LocalDateTime.now());
        when(pendingItemRepository.save(any(PendingItem.class))).thenReturn(pendingItem);
        pendingItemService.savePendingItem(pendingItem);
        verify(pendingItemRepository, atLeast(1)).save(pendingItem);
    }

    @Test
    public void testUpdatePendingItem() {
        PendingItem pendingItem = new PendingItem();
        pendingItem.setName("item1");
        pendingItem.setId(UUID.randomUUID());
        pendingItem.setCreated(LocalDateTime.now());
        when(pendingItemRepository.findById(pendingItem.getId())).thenReturn(Optional.of(pendingItem));
        pendingItemService.updatePendingItem(pendingItem);
        verify(pendingItemRepository, atLeast(1)).save(pendingItem);
    }

    @Test
    public void testUpdatePendingItemWhenIdNotFound() {
        PendingItem pendingItem = new PendingItem();
        pendingItem.setName("item1");
        pendingItem.setId(UUID.randomUUID());
        pendingItem.setCreated(LocalDateTime.now());
        when(pendingItemRepository.findById(pendingItem.getId())).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pendingItemService.updatePendingItem(pendingItem);
        });
        String expectedMessage = "item to be updated not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testDeleteItem() {
        PendingItem pendingItem = new PendingItem();
        pendingItem.setName("item1");
        pendingItem.setId(UUID.randomUUID());
        pendingItem.setCreated(LocalDateTime.now());
        when(pendingItemRepository.findById(pendingItem.getId())).thenReturn(Optional.of(pendingItem));
        pendingItemService.deletePendingItem(pendingItem.getId());
        verify(pendingItemRepository, atLeast(1)).findById(pendingItem.getId());
        verify(pendingItemRepository, atLeast(1)).delete(pendingItem);
    }

    @Test
    public void testDeleteItemNotFound() {
        PendingItem pendingItem = new PendingItem();
        pendingItem.setName("item1");
        pendingItem.setId(UUID.randomUUID());
        pendingItem.setCreated(LocalDateTime.now());
        when(pendingItemRepository.findById(pendingItem.getId())).thenReturn(Optional.empty());
        pendingItemService.deletePendingItem(pendingItem.getId());
        verify(pendingItemRepository, atLeast(1)).findById(pendingItem.getId());
        verify(pendingItemRepository, atLeast(0)).delete(pendingItem);
    }

    @Test
    public void testGetPendingItemsWithoutPaging() {
        PendingItem pendingItem = new PendingItem();
        pendingItem.setName("item1");
        pendingItem.setId(UUID.randomUUID());
        pendingItem.setCreated(LocalDateTime.now());
        when(pendingItemRepository.findAll()).thenReturn(Arrays.asList(new PendingItem[] { pendingItem }));
        when(pendingItemRepository.findByNameLike(anyString())).thenReturn(Arrays.asList(new PendingItem[] { pendingItem }));

        pendingItemService.getPendingItemsWithoutPaging(null);
        verify(pendingItemRepository, atLeast(1)).findAll();
        verify(pendingItemRepository, atLeast(0)).findByNameLike(null);

        pendingItemService.getPendingItemsWithoutPaging("search");
        verify(pendingItemRepository, atLeast(0)).findAll();
        verify(pendingItemRepository, atLeast(1)).findByNameLike("search");
    }
}
