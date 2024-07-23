package com.example.shoprunner_be.controllers;

import com.example.shoprunner_be.dtos.ProductItemDTO;
import com.example.shoprunner_be.entitys.ProductItem;
import com.example.shoprunner_be.responses.OrderResponse;
import com.example.shoprunner_be.responses.TotalPriceResponse;
import com.example.shoprunner_be.services.Cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("add-cart/{userId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<OrderResponse> addProductToCart(
            @RequestBody ProductItemDTO productItemDTO,
            @PathVariable Long userId) {
        cartService.addProductToCart(productItemDTO, userId);
        return ResponseEntity.ok(OrderResponse.builder()
                .id(userId)
                .message("Product removed from cart successfully")
                .build());
    }

    @PutMapping("/remove-one/{userId}/{productItemId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<OrderResponse> removeAProductFromCart(
            @PathVariable Long productItemId,
            @PathVariable Long userId
    ) {
        cartService.removeAProductFromCart(productItemId, userId);
        return ResponseEntity.ok(OrderResponse.builder()
                .id(userId)
                .message("Product removed from cart successfully")
                .build());
    }

    @DeleteMapping("/remove/{userId}/{productItemId}")
    public ResponseEntity<?> removeProductFromCart(
            @PathVariable Long productItemId,
            @PathVariable Long userId
    ) {
        cartService.removeProductFromCart(productItemId, userId);
        return ResponseEntity.ok("Product removed from cart successfully.");
    }

    @GetMapping("/total-price")
    public ResponseEntity<TotalPriceResponse> calculateTotalPrice(
            @RequestBody List<ProductItem> items
    ) {
        TotalPriceResponse totalPriceResponse = cartService.totalPrice(items);
        return ResponseEntity.ok(totalPriceResponse);
    }
}
