package com.example.shoprunner_be.responses.Product;

import com.example.shoprunner_be.entitys.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductResponse {
    Long id;

    String name;

    double price;

    int discount;

    double SumPrice;

    String description;

    List<ProductColorResponse> colors;

    List<ProductSizeResponse> sizes;

    List<ProductImageResponse> images;

    String category;
}
