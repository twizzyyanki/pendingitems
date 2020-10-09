package com.jahia.takehome.controller;

import com.jahia.takehome.entity.PendingItem;
import com.jahia.takehome.service.PendingItemService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("pending")
public class PendingItemController {

    private PendingItemService pendingItemService;
    private ModelMapper modelMapper = new ModelMapper();

    public PendingItemController(PendingItemService pendingItemService) {
        this.pendingItemService = pendingItemService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PendingItemDto> getPendingItem(@PathVariable String id) {
        Optional<PendingItem> itemOption = pendingItemService.getPendingItem(UUID.fromString(id));
        if (itemOption.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(itemOption.get(), PendingItemDto.class));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UUID> createPendingItem(@RequestBody PendingItemDto pendingItemDto) {
        pendingItemDto.setCreated(LocalDateTime.now());
        PendingItem pendingItem = modelMapper.map(pendingItemDto, PendingItem.class);
        pendingItemService.savePendingItem(pendingItem);
        return ResponseEntity.ok(pendingItem.getId());
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<UUID> updatePendingItem(@PathVariable String id, @RequestBody PendingItemDto pendingItemDto) {
        PendingItem pendingItem = new PendingItem();
        pendingItem.setId(UUID.fromString(id));
        pendingItem.setName(pendingItemDto.getName());
        pendingItemService.updatePendingItem(pendingItem);
        return ResponseEntity.ok(pendingItem.getId());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deletePendingItem(@PathVariable String id) {
        pendingItemService.deletePendingItem(UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/all")
    public List<PendingItemDto> getPendingItems(@RequestParam(required = false) String search) {

        return pendingItemService.getPendingItemsWithoutPaging(search).stream()
                .map(item -> modelMapper.map(item, PendingItemDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<PendingItemDto> getPendingItems(@RequestParam(required = false) Integer page,
                                                @RequestParam(required = false) String search) {
        if (page == null) {
            page = 0;
        }
        return pendingItemService.getPendingItems(page, search).stream()
                .map(item -> modelMapper.map(item, PendingItemDto.class))
                .collect(Collectors.toList());
    }
}
