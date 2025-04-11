package com.engfred.bookstore.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.engfred.bookstore.data.entities.ImageType;
import com.engfred.bookstore.dto.response.CloudinaryUploadResult;
import com.engfred.bookstore.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public CloudinaryUploadResult uploadImage(MultipartFile file, ImageType imageType) throws IOException {

        String contentType = file.getContentType();

        // Allow only specific image content types
        if (contentType == null ||
                !(contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/webp") ||
                        contentType.equals("image/gif"))) {
            throw new IllegalArgumentException("Invalid file type. Only image files (jpg, png, webp, gif) are allowed.");
        }

        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        String publicId = UUID.randomUUID().toString();
        String uploadFolder = imageType == ImageType.BOOK_COVER ? "BookCovers" : "UserImages";

        // Build transformation and upload params
        Map<?,?> uploadParams = ObjectUtils.asMap(
                "folder", uploadFolder,
                "public_id", publicId,
                "overwrite", true
        );


        var imageUrl = cloudinary.uploader().upload(convFile, uploadParams).get("url");
        return new CloudinaryUploadResult(imageUrl.toString(), uploadFolder + "/" + publicId);
    }

    @Override
    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
