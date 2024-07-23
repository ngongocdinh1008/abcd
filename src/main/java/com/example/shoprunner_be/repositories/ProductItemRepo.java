package com.example.shoprunner_be.repositories;

import com.example.shoprunner_be.entitys.ProductItem;
import org.springframework.data.repository.CrudRepository;

public interface ProductItemRepo extends CrudRepository<ProductItem, Long> {
}
