package com.example.end.infrastructure.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class ImageUploadService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }

    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public String extractPublicId(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
    }

    // Новый метод для проверки существования изображения на Cloudinary
    public boolean exists(String publicId) {
        try {
            // Пытаемся получить ресурс по publicId
            Map<String, Object> resource = cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
            return resource != null && !resource.isEmpty();
        } catch (IOException e) {
            // Если файл не найден, выбрасываем исключение
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
