package com.nus.dhproduct.controller;

import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nus.dhmodel.pojo.PriceHistory;
import com.nus.dhmodel.pojo.Product;
import com.nus.dhproduct.payload.request.CreateProductRequest;
import com.nus.dhproduct.payload.request.UpdateProductRequest;
import com.nus.dhproduct.payload.response.GeneralApiResponse;
import com.nus.dhproduct.service.ProductService;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class ProductControllerTest {
    @Mock
    ProductService productService;

    @InjectMocks
    ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {

        Product product1 = new Product();
        product1.setProductName("productname1");
        product1.setBrandName("brandname1");
        Product product2 = new Product();
        product2.setProductName("anotherproduct");
        product2.setBrandName("brandname2");
        Product product3 = new Product();
        product3.setProductName("yetanotherproduct");
        product3.setBrandName("brandname1");

        List<Product> productList = Arrays.asList(product1, product2, product3);

        when(productService.getAllProducts()).thenReturn(productList);

        ResponseEntity<List<Product>> result = productController.getAllProducts();

        Assertions.assertEquals(productList, result.getBody());
    }

    @Test
    void testGetProductByProductname() {

        Product product1 = new Product();
        product1.setProductName("productname");
        product1.setBrandName("brandname");
        product1.setLowestPrice(0d);

        List<Product> productList = Arrays.asList(product1);

        when(productService.getProductByProductname(anyString())).thenReturn(productList);

        ResponseEntity<List<Product>> result = productController.getProductByProductname("productname");
        Assertions.assertEquals(productList, result.getBody());
    }

    @Test
    void testGetProductById() {
        // 创建模拟的 Product 对象
        Product product1 = new Product();
        product1.setProductName("productname");
        product1.setBrandName("brandname");
        product1.setLowestPrice(0d);

        when(productService.getProductById(anyLong())).thenReturn(Optional.of(product1));

        ResponseEntity<Product> result = productController.getProductById(1L);

        Assertions.assertEquals(product1, result.getBody());
    }


    @Test
    void testCreateProduct() {
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setDescription("A");
        createProductRequest.setCurrentPrice(1.0);
        createProductRequest.setBrand_id(1L);
        createProductRequest.setStoreAddress("A");
        createProductRequest.setBrandname("A");
        createProductRequest.setProductname("A");
        createProductRequest.setImageUrl("A");

        when(productService.createProduct(createProductRequest)).thenReturn(new Product());
        ResponseEntity<GeneralApiResponse> response = productController.createProduct(createProductRequest);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().getSuccess());
        Assertions.assertEquals("Product created!", response.getBody().getMessage());

        verify(productService, times(1)).createProduct(createProductRequest);
    }


    @Test
    void testUpdateProduct() {
        UpdateProductRequest updateProductRequest = new UpdateProductRequest();
        updateProductRequest.setProduct_id(1L);
        updateProductRequest.setBrandname("A");
        updateProductRequest.setDescription("A");
        updateProductRequest.setLowestPrice(1.0);
        updateProductRequest.setBrand_id(1L);
        updateProductRequest.setCurrentPrice(1.0);
        updateProductRequest.setStoreAddress("A");
        updateProductRequest.setImageUrl("A");

        when(productService.updateProduct(updateProductRequest)).thenReturn(new Product());

        ResponseEntity<GeneralApiResponse> response = productController.updateProduct(updateProductRequest);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().getSuccess());
        Assertions.assertEquals("Product updated!", response.getBody().getMessage());
    }

    @Test
    void testDeleteProduct() {
        ResponseEntity<Void> result = productController.deleteProduct(Long.valueOf(1));
        Assertions.assertEquals(null, result.getBody());
    }

    @Test
    void testGetPriceHistory() {
        Product product1 = new Product();
        product1.setProductName("productname");
        product1.setBrandName("brandname");
        product1.setLowestPrice(0d);
        PriceHistory priceHistory1 = new PriceHistory();
        priceHistory1.setPrice(0d);
        priceHistory1.setId(1L);
        priceHistory1.setCreateDate(Instant.now());

        List<PriceHistory> priceHistoryList = Arrays.asList(priceHistory1);

        when(productService.getProductPriceHistory(anyLong())).thenReturn(priceHistoryList);

        ResponseEntity<List<PriceHistory>> result = productController.getProductPriceHistory(Long.valueOf(1));

        Assertions.assertEquals(priceHistoryList, result.getBody());
    }

    @Test
    void testSubmitNewPrice() {
        Product product1 = new Product();
        product1.setProductName("productname");
        product1.setBrandName("brandname");
        product1.setLowestPrice(1d);
        when(productService.submitNewPrice(anyLong(), anyDouble())).thenReturn(product1);

        ResponseEntity<Product> result = productController.submitNewPrice(Long.valueOf(1), 0d);

        Assertions.assertEquals(product1, result.getBody());
    }

}

