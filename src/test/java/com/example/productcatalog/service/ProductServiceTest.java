package com.example.productcatalog.service;

import com.example.productcatalog.model.Product;
import com.example.productcatalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Laptop", "High performance laptop",
                new BigDecimal("999.99"), "Electronics", 10,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product found = productService.getProductById(1L);

        assertNotNull(found);
        assertEquals(product.getId(), found.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getAllProducts_ShouldReturnPageOfProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<Product> result = productService.getAllProducts(pageable);

        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void createProduct_ShouldSaveProduct() {
        when(productRepository.save(product)).thenReturn(product);

        Product created = productService.createProduct(product);

        assertNotNull(created);
        assertEquals(product.getId(), created.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_ShouldUpdateExistingProduct() {
        Product updatedDetails = new Product();
        updatedDetails.setName("Updated Laptop");
        updatedDetails.setPrice(new BigDecimal("1099.99"));
        updatedDetails.setStock(15);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updated = productService.updateProduct(1L, updatedDetails);

        assertEquals("Updated Laptop", updated.getName());
        assertEquals(new BigDecimal("1099.99"), updated.getPrice());
        assertEquals(15, updated.getStock());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void deleteProduct_ShouldDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void getProductsByCategory_ShouldReturnProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findByCategory("Electronics", pageable))
                .thenReturn(List.of(product));

        List<Product> result = productService.getProductsByCategory("Electronics", pageable);

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByCategory("Electronics", pageable);
    }

    @Configuration
    @EnableCaching
    static class CachingTestConfig {
        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("products", "productsByCategory");
        }
    }
}