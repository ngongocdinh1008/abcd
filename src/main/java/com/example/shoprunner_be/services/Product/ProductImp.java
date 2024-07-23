package com.example.shoprunner_be.services.Product;

import com.example.shoprunner_be.dtos.Product.ProductDTO;
import com.example.shoprunner_be.entitys.Category;
import com.example.shoprunner_be.entitys.Product.Product;
import com.example.shoprunner_be.entitys.Product.ProductColor;
import com.example.shoprunner_be.entitys.Product.ProductSize;
import com.example.shoprunner_be.exceptions.EntityNotFoundException;
import com.example.shoprunner_be.repositories.CategoryRepo;
import com.example.shoprunner_be.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductImp implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public Product getProductById(Long id) {
        return productRepo.getProductById(id);
    }
    @Override
    public Page<Product> getAllProducts(String keyword, String category, Pageable pageable) {
        return productRepo.findAll(keyword, category, pageable);
    }

    @Override
    @Transactional
    public Product addProduct(ProductDTO productDTO) {
        Category existingCategory = categoryRepo.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found !!"));

        Product newproduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .discount(productDTO.getDiscount())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .cretedAt(LocalDateTime.now())
                .build();

        double SumPrice = (double) productDTO.getPrice() / productDTO.getDiscount();
        newproduct.setSumPrice(SumPrice);

        Optional.ofNullable(productDTO.getSizes())
                .ifPresent(sizeStrings -> {
                    List<ProductSize> sizes = sizeStrings.stream()
                            .map(sizeString -> new ProductSize(newproduct , sizeString))
                            .collect(Collectors.toList());
                    newproduct.setSizes(sizes);
                });

        Optional.ofNullable(productDTO.getColors())
                .ifPresent(colorStrings -> {
                    List<ProductColor> colors = colorStrings.stream()
                            .map(colorString -> new ProductColor(newproduct, colorString))
                            .collect(Collectors.toList());
                    newproduct.setColors(colors);
                });
        return productRepo.save(newproduct);
    }

    @Override
    @Transactional
    public Product updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id : " + productId));

        Category existingCategory = categoryRepo.getReferenceById(productDTO.getCategoryId());

        existingProduct.setName(Optional.ofNullable(productDTO.getName())
                .orElse(existingProduct.getName()));
        existingProduct.setPrice(productDTO.getPrice());
        if (productDTO.getDiscount() >= 0 && productDTO.getDiscount() <= 100) {
            existingProduct.setDiscount(productDTO.getDiscount());
        }
        existingProduct.setDescription(Optional.ofNullable(productDTO.getDescription())
                .orElse(existingProduct.getDescription()));
        existingProduct.setCategory(existingCategory);
        existingProduct.setUpdatedAt(LocalDateTime.now());

        updateSizes(existingProduct, productDTO.getSizes());
        updateColors(existingProduct, productDTO.getColors());
        updateSumPrice(existingProduct);

        return productRepo.save(existingProduct);
    }
    private void updateSumPrice(Product product) {
        double SumPrice = product.getPrice() / product.getDiscount();
        product.setSumPrice(SumPrice);
    }


    private void updateSizes(Product product, List<String> sizeStrings) {
        product.getSizes().clear();
        if (sizeStrings != null && !sizeStrings.isEmpty()) {
            List<ProductSize> sizes = sizeStrings.stream()
                    .map(sizeString -> new ProductSize(product, sizeString))
                    .toList();
            product.getSizes().addAll(sizes);
        }
    }

    private void updateColors(Product product, List<String> colorStrings) {
        product.getColors().clear();
        if (colorStrings != null && !colorStrings.isEmpty()) {
            List<ProductColor> colors = colorStrings.stream()
                    .map(colorString -> new ProductColor(product, colorString))
                    .toList();
            product.getColors().addAll(colors);
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
        productRepo.delete(existingProduct);
    }
}
