package com.example.shoprunner_be.dtos;

import jakarta.persistence.JoinColumn;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductItemDTO {
    @JoinColumn(name = "product_id")
    Long productId;
    @JoinColumn(name = "product_size")
    String productSize;
    @JoinColumn(name = "product_color")
    String productColor;
    int quantity;
}
