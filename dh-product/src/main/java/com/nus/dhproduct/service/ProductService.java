package com.nus.dhproduct.service;

import com.nus.dealhunter.exception.ProductServiceException;
import com.nus.dealhunter.model.Brand;
import com.nus.dealhunter.model.PriceHistory;
import com.nus.dealhunter.model.Product;
import com.nus.dealhunter.model.User;
import com.nus.dealhunter.payload.request.CreateProductRequest;
import com.nus.dealhunter.payload.request.UpdateProductRequest;
import com.nus.dealhunter.repository.PriceHistoryRepository;
import com.nus.dealhunter.repository.ProductRepository;
import com.nus.dealhunter.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender javaMailSender;


    public Boolean checkProductNameExists(String productname) {
        return productRepository.existsByProductname(productname);
    }

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
            return productRepository.findByProductnameContaining(productname);
        }catch (Exception e){
            throw new ProductServiceException("Failed to retrieve product with productname: " + productname, e);
        }
    }


    public List<Product> getProductByBrandname(String brandname){
        try {
            return productRepository.findByBrandname(brandname);
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
        product.setProductname(createProductRequest.getProductname());
        product.setBrandname(createProductRequest.getBrandname());
        product.setStoreAddress(createProductRequest.getStoreAddress());
        product.setDescription(createProductRequest.getDescription());
        product.setImageUrl(createProductRequest.getImageUrl());
        product.setCurrentPrice(createProductRequest.getCurrentPrice());
        product.setLowestPrice(createProductRequest.getCurrentPrice());
        product.setCreateDate(Instant.now());
        //product.setBrand(new Brand(createProductRequest.getBrand_id(),createProductRequest.getBrandname()));
        if (createProductRequest.getBrand_id() != null && createProductRequest.getBrandname() != null) {
            product.setBrand(new Brand(createProductRequest.getBrand_id(), createProductRequest.getBrandname()));
        }
        return productRepository.save(product);
    }

    public Product updateProduct(UpdateProductRequest updateProductRequest){
        Product product = new Product();
        product.setId(updateProductRequest.getProduct_id());
        product.setProductname(updateProductRequest.getProductname());
        product.setBrandname(updateProductRequest.getBrandname());
        product.setStoreAddress(updateProductRequest.getStoreAddress());
        product.setDescription(updateProductRequest.getDescription());
        product.setImageUrl(updateProductRequest.getImageUrl());
        product.setCurrentPrice(updateProductRequest.getCurrentPrice());
        //如现价低于最低价，更新最低价
        if(updateProductRequest.getCurrentPrice()<updateProductRequest.getLowestPrice()){
            product.setLowestPrice(updateProductRequest.getCurrentPrice());
            //sendLowestPriceUpdateEmails(product, updateProductRequest.getLowestPrice());
        }else {
            product.setLowestPrice(updateProductRequest.getLowestPrice());
        }
        product.setCreateDate(Instant.now());
        product.setBrand(new Brand(updateProductRequest.getBrand_id(),updateProductRequest.getBrandname()));

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
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
                    product.notify(newPrice);
//                    sendLowestPriceUpdateEmails(product, product.getLowestPrice());
                }

//                List<PriceHistory> priceHistoryList = product.getPriceHistoryList();
//                if (priceHistoryList == null) {
//                    priceHistoryList = new ArrayList<>();
//                }

                // 创建新的价格历史记录对象
                PriceHistory newPriceHistory = new PriceHistory(newPrice, Instant.now(), product);

//                //不需要 将新的价格历史记录添加到历史价格列表中
//                priceHistoryList.add(newPriceHistory);


                // 保存新的价格历史记录到数据库
                priceHistoryRepository.save(newPriceHistory);

                // 保存更新后的产品对象到数据库
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
            Optional<User> optionalUser = userRepository.findById(userId);
            Optional<Product> optionalProduct = productRepository.findById(productId);

            if (optionalUser.isPresent() && optionalProduct.isPresent()) {
                User user = optionalUser.get();
                Product product = optionalProduct.get();

                // 添加产品到用户的关注列表
//                ProductSubject productSubject = new ProductSubject(product.getId());
//                UserObserver userObserver = new UserObserver(user.getId(),product.getId());
//                productSubject.addUserObserver(userObserver);
//                userObserverRepository.save(userObserver);

                user.addWatchedProduct(product);
                userRepository.save(user);
                product.addWatcher(user);
                productRepository.save(product);
            } else {
                // 处理用户或产品不存在的情况
                throw new ProductServiceException("User or Product not found");
            }
        } catch (Exception e) {
            throw new ProductServiceException("Failed to add user to product watchers", e);
        }
    }

    public void removeUserWatchesProduct(Long userId, Long productId) {
        try {
            Optional<User> optionalUser = userRepository.findById(userId);
            Optional<Product> optionalProduct = productRepository.findById(productId);

            if (optionalUser.isPresent() && optionalProduct.isPresent()) {
                User user = optionalUser.get();
                Product product = optionalProduct.get();

                // 从产品的关注列表中移除用户
                product.removeWatcher(user);
                productRepository.save(product);

                user.removeWatchedProduct(product);
                userRepository.save(user);

            } else {
                throw new ProductServiceException("User or Product not found");
            }
        } catch (Exception e) {
            throw new ProductServiceException("Failed to remove user from product watchers", e);
        }
    }

    public boolean isUserWatchingProduct(Long userId, Long productId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalUser.isPresent() && optionalProduct.isPresent()) {
            User user = optionalUser.get();
            Product product = optionalProduct.get();

            // 检查用户是否在产品的关注列表中
            return user.getWatchedProducts().contains(product);
        } else {
            throw new ProductServiceException("User or Product not found");
        }
    }

//    //发送价格更新邮件给关注了产品的用户
//    public void sendLowestPriceUpdateEmails(Product product, double newLowestPrice) {
//
//        // 获取关注了该产品的用户列表
//        Set<User> watchers = productRepository.findUsersWatchingProduct(product);
//
//        for (User user : watchers) {
//            // 创建邮件内容
//            SimpleMailMessage message = new SimpleMailMessage();
////            message.setFrom("619176497@qq.com");
//            message.setTo(user.getEmail());
//            message.setSubject("LowestPrice Update for " + product.getProductname());
//            message.setText("The newLowestPrice for " + product.getProductname() + " has been updated to " + newLowestPrice);
//
//            // 发送邮件
//            javaMailSender.send(message);
//        }
//    }




}