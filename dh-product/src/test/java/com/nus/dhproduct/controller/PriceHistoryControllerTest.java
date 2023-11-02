package com.nus.dhproduct.controller;

import static org.mockito.Mockito.when;

import com.nus.dhmodel.pojo.PriceHistory;
import com.nus.dhproduct.exception.ProductServiceException;
import com.nus.dhproduct.payload.request.CreatePriceHistoryRequest;
import com.nus.dhproduct.service.PriceHistoryService;
import com.nus.dhproduct.service.ProductService;
import java.time.Instant;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class PriceHistoryControllerTest {

  @Mock
  PriceHistoryService priceHistoryService;

  @Mock
  ProductService productService;

  @InjectMocks
  PriceHistoryController priceHistoryController;

  @Test
  void createPriceHistory() {
    CreatePriceHistoryRequest createPriceHistoryRequest = new CreatePriceHistoryRequest();
    when(priceHistoryService.createPriceHistory(createPriceHistoryRequest)).thenReturn(null);
    Assertions.assertFalse(priceHistoryController.createPriceHistory(createPriceHistoryRequest).getBody().getSuccess());
    when(priceHistoryService.createPriceHistory(createPriceHistoryRequest)).thenReturn(new PriceHistory());
    Assertions.assertTrue(priceHistoryController.createPriceHistory(createPriceHistoryRequest).getBody().getSuccess());
  }

  @Test
  void removePriceHistory() {
    Assertions.assertEquals(HttpStatus.NO_CONTENT, priceHistoryController.removePriceHistory(1L).getStatusCode());
  }

  @Test
  void getPriceHistoryByProductId() {
    when(priceHistoryService.getPriceHistoryByProductId(1L)).thenReturn(new ArrayList<PriceHistory>());
    Assertions.assertEquals(HttpStatus.OK, priceHistoryController.getPriceHistoryByProductId(1L).getStatusCode());
  }

  @Test
  void addPriceHistoryToProduct() {
    CreatePriceHistoryRequest createPriceHistoryRequest = new CreatePriceHistoryRequest();
    when(priceHistoryService.createPriceHistory(createPriceHistoryRequest)).thenReturn(new PriceHistory());
    when(productService.addPriceHistoryToProduct(1L, new PriceHistory())).thenReturn(new PriceHistory());
    Assertions.assertEquals(HttpStatus.CREATED, priceHistoryController.addPriceHistoryToProduct(1L, createPriceHistoryRequest).getStatusCode());
    when(productService.addPriceHistoryToProduct(1L, new PriceHistory())).thenThrow(ProductServiceException.class);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, priceHistoryController.addPriceHistoryToProduct(1L, createPriceHistoryRequest).getStatusCode());
  }

  @Test
  void viewHistoricalPriceTrends() {
    Instant now = Instant.now();
    when(priceHistoryService.viewHistoricalPriceTrends(1L, now, now)).thenReturn(new ArrayList<>());
    Assertions.assertEquals(HttpStatus.OK, priceHistoryController.viewHistoricalPriceTrends(1L, now, now).getStatusCode());
  }
}