package com.engfred.bookstore.service;

import com.engfred.bookstore.data.entities.ImageType;
import com.engfred.bookstore.dto.response.CloudinaryUploadResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    CloudinaryUploadResult uploadImage(MultipartFile file, ImageType imageType) throws IOException;
    void deleteImage(String publicId) throws IOException;
}
