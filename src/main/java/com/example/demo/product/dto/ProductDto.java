package com.example.demo.product.dto;

import com.example.demo.product.model.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDto {
    String name;
    double cost;
    String description;
    private ProductCategory productCategory;
    @Schema(format = "binary")
    private MultipartFile file;
}
