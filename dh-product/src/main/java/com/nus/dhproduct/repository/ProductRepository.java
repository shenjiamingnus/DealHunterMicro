package com.nus.dhproduct.repository;

import com.nus.dhmodel.pojo.*;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByProductNameContaining(String productname);

    List<Product> findByBrandName(String brandname);

    Boolean existsByProductName(String productname);

//    Optional<Product> findByProductnameAndBrandname(String productname, String brandname);

//    // 自定义方法，查找关注了特定商品的用户
//    @Query("SELECT u FROM User u JOIN u.watchedProducts p WHERE p = :product")
//    Set<User> findUsersWatchingProduct(@Param("product") Product product);

}