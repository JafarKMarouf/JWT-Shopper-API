package com.jafarmarouf.jwtshopper.dtos;

import lombok.Data;

@Data
public class ImageDto {
    private Long id;
    private String fileName;
    private String downloadUrl;
}