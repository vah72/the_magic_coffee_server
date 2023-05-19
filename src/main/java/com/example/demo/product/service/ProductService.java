package com.example.demo.product.service;

import com.example.demo.product.awss3.BucketName;
import com.example.demo.product.cloudinary.CloudinaryService;
import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.dto.ProductResponse;
import com.example.demo.product.model.Product;
import com.example.demo.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.http.entity.ContentType.*;

@Service
public class ProductService {

    @Autowired
    private  ProductRepository productRepository;

    @Autowired
    private CloudinaryService cloudinaryService;


    public List<ProductResponse> getAllProduct() {
        List<ProductResponse> productResponses = new ArrayList<>();
        productResponses.addAll(productRepository.findAll().stream().map(product -> copyProduct(product)).collect(Collectors.toList()));
        return productResponses;
    }

    public ProductResponse getProduct(int id) {
        Product product = productRepository.findById(id);
        return copyProduct(product);
    }

    private ProductResponse copyProduct(Product product) {
        ProductResponse pResponse = new ProductResponse();
        pResponse.setImage(product.getImageLink().get());
        pResponse.setId(product.getId());
        pResponse.setDescription(product.getDescription());
        pResponse.setCost(product.getCost());
        pResponse.setName(product.getName());
        pResponse.setProductCategory(product.getProductCategory());
        return pResponse;
    }

    public Product addProduct(ProductDto productDto) {
        // 1. Check if image is not empty
        isFileEmpty(productDto.getFile());
        // 2. If file is an image
        isImage(productDto.getFile());

        String url = cloudinaryService.uploadURL(productDto.getFile());

        Product product = new Product();
        product.setName(productDto.getName());
        product.setCost(productDto.getCost());
        product.setDescription(productDto.getDescription());
        product.setProductCategory(productDto.getProductCategory());
        product.setImageLink(url);
        productRepository.save(product);
        return product;

    }

    public Product updateProduct(ProductDto productDto, int id){
        Product product = productRepository.findById(id);
        product.setName(productDto.getName());
        product.setCost(productDto.getCost());
        product.setDescription(productDto.getDescription());
        product.setProductCategory(productDto.getProductCategory());
        product.setImageLink(cloudinaryService.uploadURL(productDto.getFile()));
        productRepository.save(product);
        return product;
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType(), IMAGE_SVG.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }

    public List<ProductResponse> getSuggestionProduct() {
        List<ProductResponse> productResponses = new ArrayList<>();
        productResponses.addAll(productRepository.getSuggestionProduct("").stream().map(product -> copyProduct(product)).collect(Collectors.toList()));
        return productResponses;
    }
}
