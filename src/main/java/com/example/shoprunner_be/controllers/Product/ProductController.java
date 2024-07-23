package com.example.shoprunner_be.controllers.Product;

import com.example.shoprunner_be.dtos.Product.ProductDTO;
import com.example.shoprunner_be.entitys.Product.Product;
import com.example.shoprunner_be.exceptions.EntityNotFoundException;
import com.example.shoprunner_be.exceptions.InappropriateDataException;
import com.example.shoprunner_be.responses.Product.*;
import com.example.shoprunner_be.responses.ResponseObject;
import com.example.shoprunner_be.services.Product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                .SumPrice(product.getSumPrice())
                .description(product.getDescription())
                .colors(product.getColors().stream()
                        .map(color -> new ProductColorResponse(color.getColor()))
                        .collect(Collectors.toList()))
                .sizes(product.getSizes().stream()
                        .map(size -> new ProductSizeResponse(size.getSize()))
                        .collect(Collectors.toList()))
                .images(product.getImages().stream()
                        .map(image -> new ProductImageResponse(image.getImageUrl()))
                        .collect(Collectors.toList()))
                .category(product.getCategory().getName())
                .build());
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "", required = false) String category
    ) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            Page<Product> productPage = productService.getAllProducts(keyword, category, pageable);

            ProductListResponse productListResponse = ProductListResponse.builder()
                    .productResponses(productPage.getContent().stream().map(product -> ProductResponse.builder()
                                    .id(product.getId())
                                    .name(product.getName())
                                    .price(product.getPrice())
                                    .discount(product.getDiscount())
                                    .SumPrice(product.getSumPrice())
                                    .description(product.getDescription())
                                    .colors(product.getColors().stream()
                                            .map(color -> new ProductColorResponse(color.getColor()))
                                            .collect(Collectors.toList()))
                                    .sizes(product.getSizes().stream()
                                            .map(size -> new ProductSizeResponse(size.getSize()))
                                            .collect(Collectors.toList()))
                                    .images(product.getImages().stream()
                                            .map(image -> new ProductImageResponse(image.getImageUrl()))
                                            .collect(Collectors.toList()))
                                    .category(product.getCategory().getName())
                                    .build())
                            .collect(Collectors.toList()))
                    .totalPages(productPage.getTotalPages())
                    .build();

            return ResponseEntity.ok(productListResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unable to get products: " + e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> addProduct(@RequestBody ProductDTO productDTO) {
        Product addProduct = productService.addProduct(productDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Create new product successfully")
                .status(HttpStatus.CREATED)
                .data(addProduct)
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO) {
        Product updateProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(ResponseObject
                .builder()
                .message("Update product successfully")
                .status(HttpStatus.OK)
                .data(updateProduct)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ResponseObject
                .builder()
                .message("Delete product successfully")
                .status(HttpStatus.OK)
                .data(null)
                .build());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(InappropriateDataException.class)
    public ResponseEntity<String> handleUnsupportedImageTypeException(InappropriateDataException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
