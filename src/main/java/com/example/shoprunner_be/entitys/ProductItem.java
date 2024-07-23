package com.example.shoprunner_be.entitys;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "product_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    double price;

    int discount;

    double total;

    String color;

    String size;

    int quantity;
    @PrePersist
    public void prePersist() {
        this.discount = 1;
        this.quantity = 1;
    }
}
