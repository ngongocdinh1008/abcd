package com.example.shoprunner_be.services.Product;

import com.example.shoprunner_be.dtos.Product.ProductDTO;
import com.example.shoprunner_be.entitys.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product getProductById(Long id);

    Page<Product> getAllProducts(String keyword, String category, Pageable pageable);

    Product addProduct(ProductDTO productDTO);

    Product updateProduct(Long ProductId, ProductDTO productDTO);

    void deleteProduct(Long productId);
}

