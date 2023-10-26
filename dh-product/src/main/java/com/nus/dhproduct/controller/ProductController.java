package com.nus.dhproduct.controller;

import com.nus.dhmodel.pojo.PriceHistory;
import com.nus.dhmodel.pojo.Product;
import com.nus.dhproduct.payload.request.CreateProductRequest;
import com.nus.dhproduct.payload.request.UpdateProductRequest;
import com.nus.dhproduct.payload.response.GeneralApiResponse;
import com.nus.dhproduct.service.ProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/products")

public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if(!products.isEmpty()){
            return ResponseEntity.ok(products);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/brandname")
    public ResponseEntity<List<Product>> getProductByBrandname(@RequestParam String brandname){
        List<Product> products = productService.getProductByBrandname(brandname);
        if(!products.isEmpty()){
            return ResponseEntity.ok(products);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/productname")
    public ResponseEntity<List<Product>> getProductByProductname(@RequestParam String productname){
        List<Product> products = productService.getProductByProductname(productname);
        if(!products.isEmpty()){
            return ResponseEntity.ok(products);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<GeneralApiResponse> createProduct(@RequestBody CreateProductRequest createProductRequest){
        Product savedProduct = productService.createProduct(createProductRequest);
        if(savedProduct != null){
            return ResponseEntity.ok(new GeneralApiResponse(true,"Product created!"));
        }else {
            return ResponseEntity.ok(new GeneralApiResponse(false,"Product failed to created"));
        }
    }
    
    @PutMapping
    public ResponseEntity<GeneralApiResponse> updateProduct(@RequestBody UpdateProductRequest updateProductRequest){
        Product updatedProduct = productService.updateProduct(updateProductRequest);
        if(updatedProduct != null){
            return ResponseEntity.ok(new GeneralApiResponse(true,"Product updated!"));
        }else {
            return ResponseEntity.ok(new GeneralApiResponse(false,"Product failed to updated"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    //获取产品的价格历史记录
    @GetMapping("/getProductPriceHistory/{productId}")
    public ResponseEntity<List<PriceHistory>> getProductPriceHistory(@PathVariable Long productId) {
        List<PriceHistory> priceHistoryList = productService.getProductPriceHistory(productId);
        return ResponseEntity.ok(priceHistoryList);
    }

    @PostMapping("/{productId}/submit-price")
    public ResponseEntity<Product> submitNewPrice(
            @PathVariable Long productId,
            @RequestParam double newPrice
    ) {
        Product updatedProduct = productService.submitNewPrice(productId, newPrice);
        return ResponseEntity.ok(updatedProduct);
    }

//    @PostMapping("/{productId}/addWatchers")
//    public ResponseEntity<Void> addUserWatchesProduct(@PathVariable Long productId) {
//        productService.addUserWatchesProduct(userDetails.getId(), productId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{productId}/deleteWatchers")
//    public ResponseEntity<Void> removeUserWatchesProduct(@PathVariable Long productId) {
//        productService.removeUserWatchesProduct(userDetails.getId(), productId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @GetMapping("/{productId}/checkWatchers")
//    public ResponseEntity<Boolean> isUserWatchingProduct(@PathVariable Long productId) {
//        boolean isWatching = productService.isUserWatchingProduct(userDetails.getId(), productId);
//        return new ResponseEntity<>(isWatching, HttpStatus.OK);
//    }

//    @PostMapping("/{productId}/send-price-update-email")
//    public ResponseEntity<String> sendLowestPriceUpdateEmail(@PathVariable Long productId, @RequestParam double newLowestPrice) {
//        Product product = productService.getProductById(productId).orElse(null);
//
//        if (product != null) {
//            productService.sendLowestPriceUpdateEmails(product, newLowestPrice);
//            return new ResponseEntity<>("Price update emails sent successfully.", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("Product not found.", HttpStatus.NOT_FOUND);
//        }
//    }


}





