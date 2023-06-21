package com.shopping.product.service;

import com.shopping.product.entity.Product;
import com.shopping.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    public List<Product> addProduct(List<Product> productList) {

        return  productRepository.saveAll(productList);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }


    public List<Product> getProductsById(List<Long> productIdList) {

        return  productRepository.findAllById(productIdList);
    }
}
