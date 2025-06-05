package com.jafarmarouf.jwtshopper.services.image;

import com.jafarmarouf.jwtshopper.Dtos.ImageDto;
import com.jafarmarouf.jwtshopper.exceptions.ResourceNotFoundException;
import com.jafarmarouf.jwtshopper.models.Image;
import com.jafarmarouf.jwtshopper.models.Product;
import com.jafarmarouf.jwtshopper.repository.ImageRepository;
import com.jafarmarouf.jwtshopper.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final ProductService productService;

    /**
     * @param id Long
     * @return Image
     */
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Image found with id: " + id));
    }

    /**
     * @param files List<MultipartFile>
     * @param productId Long
     * @return List<ImageDto>
     */
    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDtos = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String buildDownloadUrl = "/api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId() + "/";
                image.setDownloadUrl(downloadUrl);

                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl(downloadUrl + savedImage.getId());

                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(image.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());

                savedImageDtos.add(imageDto);

            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDtos;
    }

    /**
     * @param file    MultipartFile
     * @param imageId Long
     * @return Image
     */
    @Override
    public Image updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            return imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @param id Long
     */
    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id)
                .ifPresentOrElse(imageRepository::delete, () -> {
                    throw new ResourceNotFoundException("No Image found with id: " + id);
                });
    }
}
