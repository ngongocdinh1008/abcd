package com.example.shoprunner_be.services.Cart;

import com.example.shoprunner_be.dtos.ProductItemDTO;
import com.example.shoprunner_be.entitys.ProductItem;
import com.example.shoprunner_be.responses.TotalPriceResponse;

import java.util.List;

public interface CartService {
    void addProductToCart(ProductItemDTO productItemDTO, Long userId);
    void removeAProductFromCart(Long productItemId, Long userId);
    void removeProductFromCart(Long productItemId, Long userId);
    TotalPriceResponse totalPrice(List<ProductItem> items);
}
