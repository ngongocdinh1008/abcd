package com.example.shoprunner_be.repositories;

import com.example.shoprunner_be.entitys.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    Product getProductById(long id);

    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR " +
            "p.name LIKE CONCAT('%', :keyword, '%') OR " +
            "p.description LIKE CONCAT('%', :keyword, '%')) AND " +
            "(:category IS NULL OR p.category.name = :category)")
    Page<Product> findAll(@Param("keyword") String keyword,
                          @Param("category") String category,
                          Pageable pageable);

}
