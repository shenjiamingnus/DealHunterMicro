package com.nus.dhbrand.service;

import com.nus.dhbrand.model.Brand;

import com.nus.dhbrand.payload.request.CreateBrandRequest;
import com.nus.dhbrand.Exception.BrandServiceException;
import com.nus.dhbrand.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;


import java.util.Optional;


@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;


    public Boolean checkBrandNameExists(String brandname) {
        return brandRepository.existsByBrandname(brandname);
    }

    public List<Brand> getAllBrands(){
        try {
            return brandRepository.findAll();
        }catch (Exception e){
            throw new BrandServiceException("Failed to retrieve all brands ", e);
        }

    }

    //    public Brand saveBrand(Brand brand) {
//        try {
//            return brandRepository.save(brand);
//        }catch (Exception e){
//            throw new BrandServiceException("Failed to save brand", e);
//        }
//
//    }
    public Brand createBrand(CreateBrandRequest createBrandRequest){
        if (createBrandRequest == null) {
            return null;
        }
        Brand brand = new Brand();
        brand.setBrandname(createBrandRequest.getBrandname());
        brand.setDescription(createBrandRequest.getDescription());
        brand.setImageUrl(createBrandRequest.getImageUrl());
        brand.setCreateDate(Instant.now());
        return brandRepository.save(brand);
    }



    public void deleteBrand(Long id) {
        try {
            brandRepository.deleteById(id);
        }catch (Exception e){
            throw new BrandServiceException("Failed to delete brand", e);
        }

    }

    public List<Brand> getBrandByBrandname(String brandname) {
        try {
            return brandRepository.findByBrandname(brandname);
        }catch (Exception e){
            throw new BrandServiceException("Failed to find brand", e);
        }
    }



    public Brand save(Brand brand){
        return brandRepository.save(brand);
    }

    public  void delete(Long id){
        brandRepository.deleteById(id);
    }


    public Optional<Brand> findById(Long id){
        return brandRepository.findById(id);
    }



}
