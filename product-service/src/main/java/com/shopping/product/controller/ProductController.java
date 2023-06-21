package com.shopping.product.controller;

import com.shopping.product.entity.Product;
import com.shopping.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RefreshScope
public class ProductController {

@Autowired
    ProductService productService;

@Value("${product.message}")
String messege;

    @PostMapping("/addProducts")
    public List<Product> addProduct (@RequestBody List<Product> productList){

        return productService.addProduct(productList);
    }
    @GetMapping("/getProducts")
    public List <Product> getProducts (){
        System.out.println("message from github ->" + messege);
        return  productService.getProducts();
    }

    @GetMapping("/getProducts/{productIdsList}")
    public  List<Product> getProductsById (@PathVariable List <Long> productIdsList){

        return productService.getProductsById(productIdsList);
    }

}
