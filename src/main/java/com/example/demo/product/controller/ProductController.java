package com.example.demo.product.controller;

import com.example.demo.common.Response;
import com.example.demo.product.dto.ProductDto;
import com.example.demo.product.dto.ProductResponse;
import com.example.demo.product.model.Product;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products/")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private  ProductService productService;

    @Autowired
    private ProductRepository productRepository;


    @GetMapping
    public ResponseEntity<?> getAllProduct() {

        return Response.response(productService.getAllProduct(), 200, "Success");
    }
    @GetMapping("{id}/")
    public ResponseEntity<?> getProduct(@PathVariable ("id") int id) {
        if(productRepository.findById(id)==null){
            return Response.response(null, 400, "Not foudn product");
        }
        return Response.response(productService.getProduct(id), 200, "Success");
    }

    @GetMapping("suggestion/")
    public ResponseEntity<?> getSuggestionProduct(){
        return Response.response(productService.getSuggestionProduct(), 200, "Success");
    }

    @PostMapping(value = "add/", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addProduct(@ModelAttribute ProductDto productDto) {
        if(productRepository.findByName(productDto.getName())!=null){
            return Response.response(null,  400, "Existed product");
        }
        return Response.response(productService.addProduct(productDto), 200, "Success");
    }
    @PutMapping(value = "update/{id}/", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateProduct(@ModelAttribute ProductDto productDto, @PathVariable("id") int id) {
        if(productRepository.findById(id)==null){
            return Response.response(null, 400, "Not found product");
        }
        return Response.response(productService.updateProduct(productDto, id), 200, "Success");
    }



}
