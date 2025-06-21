package com.example.productcatalog.controller;

import com.example.productcatalog.model.Product;
import com.example.productcatalog.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        product = new Product(1L, "Test Product", "Description",
                BigDecimal.valueOf(99.99), "Test", 10,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void getAllProducts_ShouldReturnPageOfProducts() {
        Page<Product> page = new PageImpl<>(Collections.singletonList(product));
        when(productService.getAllProducts(any())).thenReturn(page);

        ResponseEntity<Page<Product>> response = productController.getAllProducts(PageRequest.of(0, 10));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());
        verify(productService, times(1)).getAllProducts(any());
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        when(productService.getProductById(1L)).thenReturn(product);

        ResponseEntity<Product> response = productController.getProductById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Product", response.getBody().getName());
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() {
        when(productService.createProduct(any())).thenReturn(product);

        ResponseEntity<Product> response = productController.createProduct(product);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Product", response.getBody().getName());
        verify(productService, times(1)).createProduct(any());
    }
}
