package com.nus.dhproduct.service;

import com.nus.dhmodel.pojo.PriceHistory;
import com.nus.dhmodel.pojo.Product;
import com.nus.dhproduct.exception.PriceHistoryServiceException;
import com.nus.dhproduct.exception.ProductServiceException;
import com.nus.dhproduct.payload.request.CreatePriceHistoryRequest;
import com.nus.dhproduct.repository.PriceHistoryRepository;
import com.nus.dhproduct.repository.ProductRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceHistoryService {

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    private ProductRepository productRepository;

    public PriceHistory createPriceHistory(CreatePriceHistoryRequest createPriceHistoryRequest) {
        if (createPriceHistoryRequest == null) {
            return null;
        }
        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setPrice(createPriceHistoryRequest.getPrice());
        priceHistory.setCreateDate(Instant.now());
        priceHistory.setProduct(new Product(createPriceHistoryRequest.getProduct_id()));

        return priceHistoryRepository.save(priceHistory);
    }


//    public PriceHistory updatePriceHistory(PriceHistory priceHistory){
//        try {
//            return priceHistoryRepository.save(priceHistory);
//        } catch (Exception e) {
//            throw new PriceHistoryServiceException("Failed to update priceHistory ", e);
//        }
//    }

    public void deletePriceHistory(Long priceHistoryId) {
        try {
            Optional<PriceHistory> optionalProductPriceHistory = priceHistoryRepository.findById(priceHistoryId);
            if (optionalProductPriceHistory.isPresent()){
                PriceHistory priceHistory = optionalProductPriceHistory.get();
                Product product = priceHistory.getProduct();
                priceHistoryRepository.deleteById(priceHistoryId);
                if (product.getLowestPrice().equals(priceHistory.getPrice())) {
                    List<PriceHistory> priceHistoryList = product.getPriceHistoryList();
                    double newLowestPrice = Double.MAX_VALUE;
                    for(PriceHistory p : priceHistoryList){
                        newLowestPrice = Math.min(newLowestPrice, p.getPrice());
                    }
                    product.setLowestPrice(newLowestPrice);
                    productRepository.save(product);
                }
            } else {
                throw new PriceHistoryServiceException("ProductHistory with ID " + priceHistoryId + " not found");
            }
        }catch(Exception e){
            throw new PriceHistoryServiceException("Failed to delete priceHistory ", e);
        }
    }

    public List<PriceHistory> getPriceHistoryByProductId(Long productId) {
        try {
            return priceHistoryRepository.findByProductId(productId);
        }catch(Exception e){
            throw new PriceHistoryServiceException("Failed to get priceHistory by productId ", e);
        }
    }

    public List<PriceHistory> viewHistoricalPriceTrends(Long productId, Instant startDate, Instant endDate) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                // 查询价格历史记录，并根据提供的日期范围过滤数据
                List<PriceHistory> priceHistoryList = priceHistoryRepository.findByProductIdAndCreateDateBetween(productId, startDate, endDate);
                return priceHistoryList;
            } else {
                throw new ProductServiceException("Product with ID " + productId + " not found");
            }
        } catch (Exception e) {
            throw new ProductServiceException("Failed to view historical price trends for product with ID " + productId, e);
        }
    }


}
