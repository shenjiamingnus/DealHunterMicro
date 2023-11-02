package com.nus.dhproduct.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.nus.dhmodel.pojo.PriceHistory;
import com.nus.dhmodel.pojo.Product;
import com.nus.dhproduct.exception.PriceHistoryServiceException;
import com.nus.dhproduct.exception.ProductServiceException;
import com.nus.dhproduct.payload.request.CreatePriceHistoryRequest;
import com.nus.dhproduct.repository.PriceHistoryRepository;
import com.nus.dhproduct.repository.ProductRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PriceHistoryServiceTest {

  @Mock
  ProductRepository productRepository;

  @Mock
  PriceHistoryRepository priceHistoryRepository;

  @InjectMocks
  PriceHistoryService priceHistoryService;

  @Test
  void createPriceHistory() {
    PriceHistory priceHistory = new PriceHistory();
    when(priceHistoryRepository.save(ArgumentMatchers.any(PriceHistory.class))).thenReturn(priceHistory);
    Assertions.assertNull(priceHistoryService.createPriceHistory(null));
    Assertions.assertEquals(priceHistory, priceHistoryService.createPriceHistory(new CreatePriceHistoryRequest()));
  }

//  @Test
//  void updatePriceHistory() {
//  }

  @Test
  void deletePriceHistory() {
    when(priceHistoryRepository.findById(1L)).thenThrow(NullPointerException.class);
    Assertions.assertThrows(PriceHistoryServiceException.class, ()-> {
      priceHistoryService.deletePriceHistory(1L);
    });
    PriceHistory priceHistory = new PriceHistory();
    priceHistory.setPrice(2.0);
    Product product = new Product();
    product.setLowestPrice(1.0);
    product.setPriceHistoryList(new ArrayList<>());
    priceHistory.setProduct(product);
    when(priceHistoryRepository.findById(3L)).thenReturn(Optional.of(priceHistory));
    priceHistoryService.deletePriceHistory(3L);
    when(priceHistoryRepository.findById(2L)).thenThrow(NullPointerException.class);
    Assertions.assertThrows(PriceHistoryServiceException.class, ()-> {
      priceHistoryService.deletePriceHistory(2L);
    });
  }

  @Test
  void getPriceHistoryByProductId() {
    when(priceHistoryRepository.findByProductId(1L)).thenReturn(new ArrayList<>());
    priceHistoryService.getPriceHistoryByProductId(1L);
    when(priceHistoryRepository.findByProductId(2L)).thenThrow(NullPointerException.class);
    Assertions.assertThrows(PriceHistoryServiceException.class, ()-> {
      priceHistoryService.getPriceHistoryByProductId(2L);
    });
  }

  @Test
  void viewHistoricalPriceTrends() {
    when(productRepository.findById(1L)).thenThrow(NullPointerException.class);
    Instant now = Instant.now();
    Assertions.assertThrows(ProductServiceException.class, ()-> {
      priceHistoryService.viewHistoricalPriceTrends(1L, now, now);
    });
    Product product = new Product();
    when(productRepository.findById(2L)).thenReturn(Optional.of(product));
    when(priceHistoryRepository.findByProductIdAndCreateDateBetween(2L, now, now)).thenReturn(new ArrayList<>());
    Assertions.assertEquals(0, priceHistoryService.viewHistoricalPriceTrends(2L, now, now).size());
  }
}