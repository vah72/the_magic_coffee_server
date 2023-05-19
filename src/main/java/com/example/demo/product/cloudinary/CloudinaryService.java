package com.example.demo.product.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private CloudinaryConfig cloudinaryConfig;

    public CloudinaryService(CloudinaryConfig cloudinaryConfig) {
        this.cloudinaryConfig = cloudinaryConfig;
    }

    public String uploadURL(MultipartFile file){
        Cloudinary cloudinary = cloudinaryConfig.getCloudinary();
        try {
            Map upload = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return upload.get("url").toString();
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }
}
