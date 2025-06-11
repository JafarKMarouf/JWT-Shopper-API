package com.jafarmarouf.jwtshopper.controller;

import com.jafarmarouf.jwtshopper.dtos.ImageDto;
import com.jafarmarouf.jwtshopper.exceptions.ResourceNotFoundException;
import com.jafarmarouf.jwtshopper.models.Image;
import com.jafarmarouf.jwtshopper.response.ApiResponse;
import com.jafarmarouf.jwtshopper.services.image.IImageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.sql.SQLException;
import java.util.List;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@AllArgsConstructor
@RequestMapping("${api.prefix}/images/")
public class ImageController {
    private final IImageService imageService;

    @GetMapping("/image/{imageId}")
    public ResponseEntity<ApiResponse> findImageByImageId(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            ImageDto imageConverted = imageService.convertImageToDto(image);
            return ResponseEntity.ok(new ApiResponse("Image: ", imageConverted));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Image not found ", e.getMessage()));
        }
    }

    @PostMapping("/image/{productId}/upload")
    public ResponseEntity<ApiResponse> storeImage(@RequestParam List<MultipartFile> files, @PathVariable Long productId) {
        try {
            List<ImageDto> images = imageService.saveImage(files, productId);
            return ResponseEntity.ok(new ApiResponse("Uploaded Success ", images));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload Failed!", e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<?> downloadImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);

            ByteArrayResource resource = new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                    .body(resource);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Image not found ", e.getMessage()));
        } catch (SQLException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error reading image data.", e.getMessage()));
        }
    }

    @PutMapping("/image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@RequestParam MultipartFile file, @PathVariable Long imageId) {
        try {
            Image image = imageService.getImageById(imageId);
            if (image != null) {
                image = imageService.updateImage(file, imageId);
                ImageDto imageConverted = imageService.convertImageToDto(image);
                return ResponseEntity.ok(new ApiResponse("Image updated successfully ", imageConverted));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Image not found ", e.getMessage()));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Updated Failed!", null));
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImageByImageId(@PathVariable Long imageId) {
        try {
            imageService.deleteImageById(imageId);
            return ResponseEntity.ok(new ApiResponse("Image deleted success for id: " + imageId, null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Image not found for id: " + imageId, null));
        }
    }
}
