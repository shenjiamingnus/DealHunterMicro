package com.nus.dhbrand.repository;

import com.nus.dhmodel.pojo.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    List<Brand> findByBrandname(String brandname);

    Boolean existsByBrandname(String brandname);


}
