package com.nus.dhbrand.controller;
import com.nus.dhbrand.model.Brand;

import com.nus.dhbrand.payload.request.CreateBrandRequest;
import com.nus.dhbrand.payload.response.GeneralApiResponse;
import com.nus.dhbrand.service.BrandService;
import com.nus.dhbrand.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("Brand/")
@RestController
@RequestMapping("/api/brands")

public class BrandController {
    @Autowired
    private BrandService brandService;


    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping
    public List<Brand> getAllBrands() {
        return brandService.getAllBrands();
    }

    @PostMapping
    public ResponseEntity<GeneralApiResponse> createBrand(@RequestBody CreateBrandRequest createBrandRequest){
        Brand savedBrand = brandService.createBrand(createBrandRequest);
        if(savedBrand != null){
            return ResponseEntity.ok(new GeneralApiResponse(true,"Brand created!"));
        }else {
            return ResponseEntity.ok(new GeneralApiResponse(false,"Brand failed to created"));
        }

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id){
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/brandname")
    public ResponseEntity<List<Brand>> getBrandByBrandname(@RequestParam String brandname){
        List<Brand> brands = brandService.getBrandByBrandname(brandname);
        if(!brands.isEmpty()){
            return ResponseEntity.ok(brands);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

}
