package com.shopping.shoppingcart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping.shoppingcart.entity.CartEntity;
import com.shopping.shoppingcart.model.Product;
import com.shopping.shoppingcart.model.ShoppingCartRequest;
import com.shopping.shoppingcart.model.ShoppingCartResponse;
import com.shopping.shoppingcart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

  @Autowired
  @Qualifier("webClient")
    private  WebClient.Builder webBuilder;

    @Autowired
    CartRepository cartRepository;
    public ShoppingCartResponse processAddRequest(Long userId,  List<ShoppingCartRequest> shoppingCartRequestList) {
        //call product service
        ObjectMapper mapper = new ObjectMapper();
       // String productServiceUrl = "http://localhost:8081/products/getProducts/" + shoppingCartRequestList.stream().map(e -> String.valueOf(e.getProductId())).collect(Collectors.joining(","));
        String productServiceUrl = "http://product-service/products/getProducts/" + shoppingCartRequestList.stream().map(e -> String.valueOf(e.getProductId())).collect(Collectors.joining(","));

        List <Product> productServiceList = webBuilder.build()
                .get()
                .uri(productServiceUrl)
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .block();

      //  System.out.println(productServiceUrl);
       // System.out.println("Product service list" + productServiceList);
        // calculate total cost
        final Double[] totalCost= {0.0};
        productServiceList.forEach(psl->{
            shoppingCartRequestList.forEach(scr->{
                if(psl.getProductId()== scr.getProductId()){
                    psl.setQuantity(scr.getQuantity());
                    totalCost[0]= totalCost[0] + psl.getAmount()* scr.getQuantity();
                }
            });
        });
        //create cart entity
        CartEntity cartEntity = null;
        try {
             cartEntity = CartEntity.builder()
                    .userId(userId)
                    .cartId((long)(Math.random()*Math.pow(10,10)))
                    .totalItems(productServiceList.size())
                    .totalCost(totalCost[0])
                    .products(mapper.writeValueAsString(productServiceList))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // save cart entity
       cartEntity = cartRepository.save(cartEntity);
        //create api response
        ShoppingCartResponse response = ShoppingCartResponse.builder()
                .cartId(cartEntity.getCartId())
                .userId(cartEntity.getUserId())
                .totalItems(cartEntity.getTotalItems())
                .totalCost(cartEntity.getTotalCost())
                .products(productServiceList)
                .build();

        return  response;
    }


    public List<ShoppingCartResponse> getShoppingCart(Long userId) {
        ObjectMapper mapper = new ObjectMapper();     //convert string to pojo
       List <CartEntity> cartEntities = cartRepository.findByUserId(userId);
        List<ShoppingCartResponse> cartResponses = cartEntities.stream()
                .map(ce->{
                    try {
                        return  ShoppingCartResponse.builder()
                                .cartId(ce.getCartId())
                                .userId(ce.getUserId())
                                .totalItems(ce.getTotalItems())
                                .totalCost(ce.getTotalCost())
                                .products(mapper.readValue(ce.getProducts(),List.class))
                                .build();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());


       return  cartResponses;
    }
}
