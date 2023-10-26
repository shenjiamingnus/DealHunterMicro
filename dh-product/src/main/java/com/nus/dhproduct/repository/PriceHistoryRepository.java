package com.nus.dhproduct.repository;


import com.nus.dhmodel.pojo.PriceHistory;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    // 查询指定商品在指定日期范围内的价格历史记录
    List<PriceHistory> findByProductIdAndCreateDateBetween(Long productId, Instant startDate, Instant endDate);

    // 查询指定商品的所有价格历史记录
    List<PriceHistory> findByProductId(Long productId);

//    // 查询指定ID价格历史记录
//    Optional<PriceHistory> findById(Long Id);


}
