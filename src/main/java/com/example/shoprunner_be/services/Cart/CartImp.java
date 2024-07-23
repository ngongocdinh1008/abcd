package com.example.shoprunner_be.services.Cart;

import com.example.shoprunner_be.dtos.ProductItemDTO;
import com.example.shoprunner_be.entitys.Cart;
import com.example.shoprunner_be.entitys.Product.Product;
import com.example.shoprunner_be.entitys.ProductItem;
import com.example.shoprunner_be.entitys.User;
import com.example.shoprunner_be.exceptions.EntityNotFoundException;
import com.example.shoprunner_be.repositories.CartRepo;
import com.example.shoprunner_be.repositories.ProductItemRepo;
import com.example.shoprunner_be.responses.TotalPriceResponse;
import com.example.shoprunner_be.services.Product.ProductService;
import com.example.shoprunner_be.services.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartImp implements CartService {
    private final CartRepo cartRepo;
    private final UserService userService;
    private final ProductService productService;
    private final ProductItemRepo productItemRepo;

    private Cart getCartByUserId(Long userId) {
        User user = userService.getUserById(userId);
        if (user.getCart() != null) {
            return user.getCart();
        }
        return null;
    }
    @Override
    public void addProductToCart(ProductItemDTO productItemDTO, Long userId) {
        Optional<Cart> cartOptional = cartRepo.findByUserId(userId);
        User user = userService.getUserById(userId);
        Cart cart;

        if (cartOptional.isEmpty()) {
            cart = Cart.builder()
                    .products(new ArrayList<>())
                    .user(user)
                    .build();
            cartRepo.save(cart);
        } else {
            cart = cartOptional.get();
        }

        Product product = productService.getProductById(productItemDTO.getProductId());
        boolean productExists = false;

        for (ProductItem item : cart.getProducts()) {
            if (item.getName().equals(product.getName()) &&
                    item.getColor().equals(productItemDTO.getProductColor()) &&
                    item.getSize().equals(productItemDTO.getProductSize())) {
                item.setQuantity(item.getQuantity() + productItemDTO.getQuantity());
                item.setTotal((double) (product.getPrice() / product.getDiscount()) * item.getQuantity());
                productExists = true;
                break;
            }
        }

        if (!productExists) {
            ProductItem newItem = ProductItem.builder()
                    .name(product.getName())
                    .color(productItemDTO.getProductColor())
                    .size(productItemDTO.getProductSize())
                    .quantity(productItemDTO.getQuantity())
                    .price(product.getPrice())
                    .discount(product.getDiscount())
                    .total((double) (product.getPrice() / product.getDiscount()) * productItemDTO.getQuantity())
                    .build();
            cart.getProducts().add(newItem);
        }

        cartRepo.save(cart);
    }
    @Override
    public void removeAProductFromCart(Long productItemId, Long userId) {
        Cart cart = getCartByUserId(userId);
        if (cart == null) {
            throw new EntityNotFoundException("Cart not found for user id: " + userId);
        }

        ProductItem item = productItemRepo.findById(productItemId).orElseThrow(() ->
                new EntityNotFoundException("Product item not found for id: " + productItemId));

        Iterator<ProductItem> iterator = cart.getProducts().iterator();
        while (iterator.hasNext()) {
            ProductItem productItem = iterator.next();
            if (productItem.getName().equals(item.getName()) &&
                    productItem.getColor().equals(item.getColor()) &&
                    productItem.getSize().equals(item.getSize())) {
                if (productItem.getQuantity() > 1) {
                    productItem.setQuantity(productItem.getQuantity() - 1);
                    productItem.setTotal((double) (productItem.getPrice() / productItem.getDiscount()) * productItem.getQuantity());
                } else {
                    iterator.remove();
                }
                break;
            }
        }

        cartRepo.save(cart);
    }
    @Override
    public void removeProductFromCart(Long productItemId, Long userId) {
        Cart cart = getCartByUserId(userId);
        if (cart == null) {
            throw new EntityNotFoundException("Cart not found for user id: " + userId);
        }
        cart.getProducts().removeIf(item -> item.getId().equals(productItemId));
        cartRepo.save(cart);

    }
    @Override
    public TotalPriceResponse totalPrice(List<ProductItem> items){
        double totalAmount = 0.0;
        int totalQuantity = 0;
        for (ProductItem item : items) {
            totalAmount += item.getTotal();
            totalQuantity += item.getQuantity();
        }
        return TotalPriceResponse.builder()
                .price(totalAmount)
                .quantity(totalQuantity)
                .build();
    }

}
