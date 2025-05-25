package com.peersmarket.marketplace.item.application.model;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageFileWrapper {
    private final InputStream inputStream;
    private final String originalFilename;
    private final String contentType;
    private final long size;
    private final String keyPrefix; // ex: "items/" ou "items/{itemId}/" pour organiser dans S3
}
