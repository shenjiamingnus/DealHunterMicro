package com.nus.dhbrand.controller;
import com.nus.dhmodel.pojo.Brand;
import com.nus.dhbrand.payload.request.CreateBrandRequest;
import com.nus.dhbrand.payload.request.ModifyBrandRequest;
import com.nus.dhbrand.payload.response.GeneralApiResponse;
import com.nus.dhbrand.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public List<Brand> getAllBrands() {
        return brandService.getAllBrands();
    }

    @PostMapping
    public ResponseEntity<GeneralApiResponse> createBrand(@RequestBody CreateBrandRequest createBrandRequest, @RequestHeader("isAdmin") int isAdmin){
        if (isAdmin != 1) {
          return ResponseEntity.ok(new GeneralApiResponse(false,"Not Admin User!"));
        }
        Brand savedBrand = brandService.createBrand(createBrandRequest);
        if(savedBrand != null){
            return ResponseEntity.ok(new GeneralApiResponse(true,"Brand created!"));
        }else {
            return ResponseEntity.ok(new GeneralApiResponse(false,"Brand failed to created"));
        }
    }

    @PutMapping
    public ResponseEntity<GeneralApiResponse> modifyBrand(@RequestBody ModifyBrandRequest modifyBrandRequest, @RequestHeader("isAdmin") int isAdmin){
        if (isAdmin != 1) {
          return ResponseEntity.ok(new GeneralApiResponse(false,"Not Admin User!"));
        }
        Brand modifiedBrand = brandService.modifyBrand(modifyBrandRequest);
        if(modifiedBrand != null){
            return ResponseEntity.ok(new GeneralApiResponse(true,"Brand modified!"));
        }else {
            return ResponseEntity.ok(new GeneralApiResponse(false,"Brand failed to modify"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralApiResponse> deleteBrand(@PathVariable Long id, @RequestHeader("isAdmin") int isAdmin){
        if (isAdmin != 1) {
          return ResponseEntity.ok(new GeneralApiResponse(false,"Not Admin User!"));
        }
        brandService.deleteBrand(id);
      return ResponseEntity.ok(new GeneralApiResponse(true,"Brand Deleted!"));
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
