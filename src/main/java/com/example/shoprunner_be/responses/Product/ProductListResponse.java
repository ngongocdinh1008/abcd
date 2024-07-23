package com.example.shoprunner_be.responses.Product;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductListResponse {
     List<ProductResponse> productResponses;
     int totalPages;
}
