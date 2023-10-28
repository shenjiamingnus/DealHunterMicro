package com.nus.dhproduct.controller;

import com.nus.dhmodel.pojo.PriceHistory;
import com.nus.dhproduct.exception.ProductServiceException;
import com.nus.dhproduct.payload.request.CreatePriceHistoryRequest;
import com.nus.dhproduct.payload.response.GeneralApiResponse;
import com.nus.dhproduct.service.PriceHistoryService;
import com.nus.dhproduct.service.ProductService;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PriceHistoryController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PriceHistoryService priceHistoryService;


    @PostMapping("/price-history/create")
    public ResponseEntity<GeneralApiResponse> createPriceHistory(@RequestBody CreatePriceHistoryRequest createPriceHistoryRequest) {
        PriceHistory savedPriceHistory = priceHistoryService.createPriceHistory(createPriceHistoryRequest);
        if(savedPriceHistory != null){
            return ResponseEntity.ok(new GeneralApiResponse(true,"PriceHistory created!"));
        }else {
            return ResponseEntity.ok(new GeneralApiResponse(false,"PriceHistory failed to created"));
        }
    }

    @DeleteMapping("/price-history/remove/{priceHistoryId}")
    public ResponseEntity<Void> removePriceHistory(@PathVariable Long priceHistoryId) {
        priceHistoryService.deletePriceHistory(priceHistoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/price-history/product/{productId}")
    public ResponseEntity<List<PriceHistory>> getPriceHistoryByProductId(@PathVariable Long productId) {
        List<PriceHistory> priceHistoryList = priceHistoryService.getPriceHistoryByProductId(productId);
        return new ResponseEntity<>(priceHistoryList, HttpStatus.OK);
    }

    @PostMapping("/price-history/product/{productId}/add")
    public ResponseEntity<PriceHistory> addPriceHistoryToProduct(
            @PathVariable Long productId,
            @RequestBody CreatePriceHistoryRequest createPriceHistoryRequest) {
        try {
            PriceHistory newPriceHistory = priceHistoryService.createPriceHistory(createPriceHistoryRequest);
            PriceHistory addedPriceHistory = productService.addPriceHistoryToProduct(productId, newPriceHistory);
            return new ResponseEntity<>(addedPriceHistory, HttpStatus.CREATED);
        } catch (ProductServiceException e) {
            // 处理异常并返回适当的响应
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/price-history/{Id}")
    public ResponseEntity<List<PriceHistory>> viewHistoricalPriceTrends(
            @PathVariable Long Id,
            @RequestParam Instant startDate,
            @RequestParam Instant endDate) {
        List<PriceHistory> priceHistoryList = priceHistoryService.viewHistoricalPriceTrends(Id, startDate, endDate);
        return ResponseEntity.ok(priceHistoryList);
    }

}
