package com.nus.dhproduct.service;

import com.alibaba.nacos.common.utils.JacksonUtils;
import com.nus.dhmodel.dto.EmailContent;
import com.nus.dhmodel.pojo.PriceHistory;
import com.nus.dhmodel.pojo.Product;
import com.nus.dhmodel.pojo.User;
import com.nus.dhproduct.exception.ProductServiceException;
import com.nus.dhproduct.feign.UserFeignService;
import com.nus.dhproduct.payload.request.CreateProductRequest;
import com.nus.dhproduct.payload.request.UpdateProductRequest;
import com.nus.dhproduct.repository.PriceHistoryRepository;
import com.nus.dhproduct.repository.ProductRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    private UserFeignService userFeignService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

//    public Boolean checkProductNameExists(String productname) {
//        return productRepository.existsByProductName(productname);
//    }

    public List<Product> getAllProducts(){
        try {
            return productRepository.findAll();
        }catch (Exception e){
            throw new ProductServiceException("Failed to retrieve all product ", e);
        }
    }

    public Optional<Product> getProductById(Long id) throws ProductServiceException {
        try {
            return productRepository.findById(id);
        }catch (Exception e){
            throw new ProductServiceException("Failed to retrieve product with ID: " + id, e);
        }
    }

    public List<Product> getProductByProductname(String productname) {
        try {
            return productRepository.findByProductNameContaining(productname);
        }catch (Exception e){
            throw new ProductServiceException("Failed to retrieve product with productname: " + productname, e);
        }
    }

    public List<Product> getProductByBrandname(String brandname){
        try {
            return productRepository.findByBrandName(brandname);
        }catch (Exception e){
            throw new ProductServiceException("Failed to retrieve product with Brandname: " + brandname, e);
        }
    }

    //when create a product, make LowestPrice = CurrentPrice
    public Product createProduct(CreateProductRequest createProductRequest){
        if (createProductRequest == null) {
            return null;
        }
        Product product = new Product();
        product.setProductName(createProductRequest.getProductname());
        product.setBrandName(createProductRequest.getBrandname());
        product.setStoreAddress(createProductRequest.getStoreAddress());
        product.setDescription(createProductRequest.getDescription());
        product.setImageUrl(createProductRequest.getImageUrl());
        product.setCurrentPrice(createProductRequest.getCurrentPrice());
        product.setLowestPrice(createProductRequest.getCurrentPrice());
        product.setBrandId(createProductRequest.getBrand_id());
        product.setWatcherUserId(new HashSet<>());
        product.setCreateDate(Instant.now());
        // 创建新的价格历史记录对象
        Product credatedProduct = productRepository.save(product);

        // 创建新的价格历史记录对象
        PriceHistory newPriceHistory = new PriceHistory();
        newPriceHistory.setProduct(product);
        newPriceHistory.setCreateDate(Instant.now());
        newPriceHistory.setPrice(credatedProduct.getCurrentPrice());
        priceHistoryRepository.save(newPriceHistory);

        return credatedProduct;
    }

    public Product updateProduct(UpdateProductRequest updateProductRequest){
        Product product = productRepository.findById(updateProductRequest.getProduct_id()).get();
        BeanUtils.copyProperties(updateProductRequest, product);
        product.setProductName(updateProductRequest.getProductname());
        product.setStoreAddress(updateProductRequest.getStoreAddress());
        product.setDescription(updateProductRequest.getDescription());
        return productRepository.save(product);
    }

    public boolean deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
            return true;
        }catch (Exception e){
            throw new ProductServiceException("Failed to delete product", e);
        }
    }

    public List<PriceHistory> getProductPriceHistory(Long productId) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                // 获取产品的历史价格列表
                List<PriceHistory> priceHistoryList = product.getPriceHistoryList();
                return priceHistoryList;
            } else {
                throw new ProductServiceException("Product with ID " + productId + " not found");
            }
        } catch (Exception e) {
            throw new ProductServiceException("Failed to retrieve price history for product with ID " + productId, e);
        }
    }

    // 向商品的价格历史记录列表添加一条记录
    public PriceHistory addPriceHistoryToProduct(Long productId, PriceHistory priceHistory) throws ProductServiceException {
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                priceHistory.setProduct(product);                       // 关联到商品
                PriceHistory savedPriceHistory = priceHistoryRepository.save(priceHistory);
                product.getPriceHistoryList().add(savedPriceHistory);   // 添加到商品的价格历史记录列表
                productRepository.save(product);                        // 更新商品对象，以保存关联的价格历史记录
                return savedPriceHistory;
            } else {
                throw new ProductServiceException("Product with ID " + productId + " not found");
            }
        } catch (Exception e) {
            throw new ProductServiceException("Failed to add price history to product with ID " + productId, e);
        }
    }

    public void deletePriceHistoryFromProduct(Long productId, Long priceHistoryId) {
        try {
            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                // 获取产品的价格历史记录列表
                List<PriceHistory> priceHistoryList = product.getPriceHistoryList();

                // 遍历价格历史记录列表，查找要删除的记录
                for (PriceHistory priceHistory : priceHistoryList) {
                    if (priceHistory.getId().equals(priceHistoryId)) {
                        priceHistoryList.remove(priceHistory);
                        productRepository.save(product);
                        return;
                    }
                }
            }
        }catch (Exception e){
                throw new ProductServiceException("Failed to delete price history to product with ID " + productId, e);
        }
    }

    public Product submitNewPrice(Long productId, double newPrice) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();

                // 更新当前价格
                product.setCurrentPrice(newPrice);

                // 如果新价格低于历史最低价或历史最低价为0，更新历史最低价，并发送邮件
                if (newPrice < product.getLowestPrice() || product.getLowestPrice() == 0) {
                    product.setLowestPrice(newPrice);
                    sendLowestPriceUpdateEmails(product, product.getLowestPrice());
                }

                PriceHistory newPriceHistory = new PriceHistory();
                newPriceHistory.setPrice(newPrice);
                newPriceHistory.setProduct(product);
                newPriceHistory.setCreateDate(Instant.now());

                priceHistoryRepository.save(newPriceHistory);
                return productRepository.save(product);
            } else {
                throw new ProductServiceException("Product with productId " + productId + " not found");
            }
        } catch (Exception e) {
            throw new ProductServiceException("Failed to submit new price for product with productId " + productId, e);
        }
    }

    public void addUserWatchesProduct(Long userId, Long productId) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                Set<Long> watcherUserId = product.getWatcherUserId();
                watcherUserId.add(userId);
                product.setWatcherUserId(watcherUserId);
                productRepository.save(product);
            } else {
                throw new ProductServiceException("User or Product not found");
            }
        } catch (Exception e) {
            throw new ProductServiceException("Failed to add user to product watchers", e);
        }
    }

    public void removeUserWatchesProduct(Long userId, Long productId) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.getWatcherUserId().remove(userId);
                productRepository.save(product);
            } else {
                throw new ProductServiceException("User or Product not found");
            }
        } catch (Exception e) {
            throw new ProductServiceException("Failed to remove user from product watchers", e);
        }
    }

    public boolean isUserWatchingProduct(Long userId, Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            // 检查用户是否在产品的关注列表中
            return product.getWatcherUserId().contains(userId);
        } else {
            throw new ProductServiceException("User or Product not found");
        }
    }

    //发送价格更新邮件给关注了产品的用户
    public void sendLowestPriceUpdateEmails(Product product, double newLowestPrice) {
      // 获取关注了该产品的用户列表
      Set<Long> watcherUserIds = product.getWatcherUserId();
      List<Long> idList = new ArrayList<>(watcherUserIds);
      List<User> watchers = userFeignService.getUserById(idList);

      for (User user : watchers) {
        EmailContent emailContent = new EmailContent();
        emailContent.setEmail(user.getEmail());
        emailContent.setPrice(newLowestPrice);
        emailContent.setProductName(product.getProductName());
        rabbitTemplate.convertAndSend(queue.getName(), JacksonUtils.toJson(emailContent));
      }
    }

}