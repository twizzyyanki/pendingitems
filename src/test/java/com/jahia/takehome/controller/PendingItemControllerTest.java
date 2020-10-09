package com.jahia.takehome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jahia.takehome.entity.PendingItem;
import com.jahia.takehome.service.PendingItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PendingItemController.class)
public class PendingItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PendingItemService pendingItemService;

    private ModelMapper modelMapper = new ModelMapper();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetPendingItem() throws Exception {
        PendingItem pendingItem = new PendingItem();
        pendingItem.setName("item");
        pendingItem.setCreated(LocalDateTime.now());
        pendingItem.setId(UUID.randomUUID());

        given(pendingItemService.getPendingItem(pendingItem.getId())).willReturn(Optional.of(pendingItem));

        mockMvc.perform(get("/pending/" + pendingItem.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(pendingItem.getName())));

        given(pendingItemService.getPendingItem(pendingItem.getId())).willReturn(Optional.empty());
        mockMvc.perform(get("/pending/" + pendingItem.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreatePendingItem() throws Exception {
        PendingItemDto pendingItemDto = new PendingItemDto();
        pendingItemDto.setName("item");

        mockMvc.perform(post("/pending")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(pendingItemDto)))
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdatePendingItem() throws Exception {
        PendingItem pendingItem = new PendingItem();
        pendingItem.setName("item");
        pendingItem.setCreated(LocalDateTime.now());
        pendingItem.setId(UUID.randomUUID());

        PendingItemDto pendingItemDto = new PendingItemDto();
        pendingItemDto.setName(pendingItem.getName());

        mockMvc.perform(post("/pending/" + pendingItem.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(pendingItemDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletePendingItem() throws Exception {
        PendingItem pendingItem = new PendingItem();
        pendingItem.setName("item");
        pendingItem.setCreated(LocalDateTime.now());
        pendingItem.setId(UUID.randomUUID());

        PendingItemDto pendingItemDto = new PendingItemDto();
        pendingItemDto.setName(pendingItem.getName());

        mockMvc.perform(delete("/pending/" + pendingItem.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPendingItems() throws Exception {
        PendingItem pendingItem = new PendingItem();
        pendingItem.setName("item");
        pendingItem.setCreated(LocalDateTime.now());
        pendingItem.setId(UUID.randomUUID());

        given(pendingItemService.getPendingItemsWithoutPaging(anyString())).willReturn(Collections.singletonList(pendingItem));

        mockMvc.perform(get("/pending/all?search=search"))
                .andExpect(status().isOk());
    }
}
