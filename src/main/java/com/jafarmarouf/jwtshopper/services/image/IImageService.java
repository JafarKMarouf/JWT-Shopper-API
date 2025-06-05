package com.jafarmarouf.jwtshopper.services.image;

import com.jafarmarouf.jwtshopper.Dtos.ImageDto;
import com.jafarmarouf.jwtshopper.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);

    List<ImageDto> saveImage(List<MultipartFile> files, Long productId);

    Image updateImage(MultipartFile image, Long imageId);

    void deleteImageById(Long id);
}
