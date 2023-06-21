package com.shopping.shoppingcart.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShoppingCartRequest {
    private Long productId;
    private Integer quantity;

}
