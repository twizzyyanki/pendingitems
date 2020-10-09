package com.jahia.takehome.controller;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PendingItemDto {

    private UUID id;

    private String name;

    private LocalDateTime created;
}
