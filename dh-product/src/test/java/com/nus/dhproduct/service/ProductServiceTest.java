package com.nus.dhproduct.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import com.nus.dhmodel.pojo.Brand;
import com.nus.dhmodel.pojo.Product;
import com.nus.dhproduct.feign.UserFeignService;
import com.nus.dhproduct.payload.request.CreateProductRequest;
import com.nus.dhproduct.payload.request.UpdateProductRequest;
import com.nus.dhproduct.repository.PriceHistoryRepository;
import com.nus.dhproduct.repository.ProductRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    PriceHistoryRepository priceHistoryRepository;

    @Mock
    UserFeignService userFeignService;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Mock
    Queue queue;

    @InjectMocks
    ProductService productService;

    @Test
    void testGetAllProducts(){
        List<Product> productList = new ArrayList<>();
        when(productRepository.findAll()).thenReturn(productList);
        Assertions.assertEquals(productList, productService.getAllProducts());
    }

    @Test
    void testGetProductById(){
        Product product = new Product();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Assertions.assertEquals(product, productService.getProductById(1L).get());
    }

    @Test
    void getProductByProductname() {
        List<Product> productList = new ArrayList<>();
        when(productRepository.findByProductNameContaining("a")).thenReturn(productList);
        Assertions.assertEquals(productList, productService.getProductByProductname("a"));
    }

    @Test
    void getProductByBrandname() {
        List<Product> productList = new ArrayList<>();
        when(productRepository.findByBrandName("a")).thenReturn(productList);
        Assertions.assertEquals(productList, productService.getProductByBrandname("a"));
    }

    @Test
    void createProduct() {
        Product product = new Product();
        CreateProductRequest createProductRequest = new CreateProductRequest();
        when(productRepository.save(ArgumentMatchers.any(Product.class))).thenReturn(product);
        Assertions.assertEquals(product, productService.createProduct(createProductRequest));
    }

    @Test
    void updateProduct() {
        Product product = new Product();
        UpdateProductRequest updateProductRequest = new UpdateProductRequest();
        updateProductRequest.setProduct_id(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(ArgumentMatchers.any(Product.class))).thenReturn(product);
        Assertions.assertEquals(product, productService.updateProduct(updateProductRequest));
    }

    @Test
    void deleteProduct() {
        Assertions.assertTrue(productService.deleteProduct(1L));
    }

    @Test
    void getProductPriceHistory() {

    }

    @Test
    void addPriceHistoryToProduct() {
    }

    @Test
    void deletePriceHistoryFromProduct() {
    }

    @Test
    void submitNewPrice() {
    }

    @Test
    void addUserWatchesProduct() {
    }

    @Test
    void removeUserWatchesProduct() {
    }

    @Test
    void isUserWatchingProduct() {
    }

    @Test
    void sendLowestPriceUpdateEmails() {
    }
}
