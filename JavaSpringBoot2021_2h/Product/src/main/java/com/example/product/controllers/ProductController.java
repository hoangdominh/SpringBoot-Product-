package com.example.product.controllers;


import com.example.product.models.Product;
import com.example.product.models.ResponseObject;
import com.example.product.repositories.ProductRepositoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/Products") //RequestMapping : Ánh xạ các request
public class ProductController {
    //DI : dependency injection - Tiêm phụ thuộc
    @Autowired
    private ProductRepositoryDAO productRepositoryDAO;

    @GetMapping("")
    List<Product> getAllProducts(){
        return productRepositoryDAO.findAll();
        //Bạn phải lưu dữ liệu vào Database
    }

    @GetMapping("/{id}")

    //    // Optional : giá trị trả về có thể là null
    //    Optional<Product> findById(@PathVariable Long id){
    //        return productRepositoryDAO.findById(id);
    //    }


    //    Chuẩn hóa đối tượng trả về
    ResponseEntity<ResponseObject> findById(@PathVariable Long id){
        Optional<Product> foundProduct = productRepositoryDAO.findById(id);
        if(foundProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","Query products",foundProduct));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Failed","Cannot find product with id = "+id,""));
        }
    }


    @PostMapping("/insert")
    // Insert new Product with POST method
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        // Check validate
        List<Product> foundProducts = productRepositoryDAO.findByProductName(newProduct.getProductName().trim());
        if (foundProducts.size() > 0 ){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("Error","Product name already taken",""));
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("OK","Insert Product Successfully",productRepositoryDAO.save(newProduct)));
    }

    // Update, upsert = update if found, otherwise insert : Nếu không tìm được sản phầm để update thì insert

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id){

        Product updateProduct = productRepositoryDAO.findById(id).map(product ->
        {
          product.setProductName(newProduct.getProductName());
          product.setYear(newProduct.getYear());
          product.setPrice(newProduct.getPrice());
          product.setUrl(newProduct.getUrl());
          return productRepositoryDAO.save(product);
        }).orElseGet(() -> {
            newProduct.setId(id);
            return productRepositoryDAO.save(newProduct);
        });
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","Update Product Successfully",updateProduct));
    }

    // Delete a Product ==> Delete method
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id){
        boolean exists = productRepositoryDAO.existsById(id);
        if(exists) {
            productRepositoryDAO.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","Delete product successfully",""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("Failed","Cannot find product to delete",""));
    }

}
