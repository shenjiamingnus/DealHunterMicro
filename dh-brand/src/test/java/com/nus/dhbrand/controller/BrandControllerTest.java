package com.nus.dhbrand.controller;

import com.nus.dhbrand.payload.request.CreateBrandRequest;
import com.nus.dhbrand.payload.request.ModifyBrandRequest;
import com.nus.dhbrand.service.BrandService;
import com.nus.dhmodel.pojo.Brand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
class BrandControllerTest {

    @Mock
    private BrandService brandService;

    @InjectMocks
    BrandController brandController;

    @Test
    void testGetAllBrands(){
        Brand brand1 = new Brand();
        brand1.setBrandname("Brandname1");
        Brand brand2 = new Brand();
        brand2.setBrandname("anotherBrand");
        Brand brand3 = new Brand();
        brand3.setBrandname("yetanotherBrand");

        List<Brand> brandList = Arrays.asList(brand1, brand2, brand3);

        when(brandService.getAllBrands()).thenReturn(brandList);
        List<Brand> result = brandController.getAllBrands();
        Assertions.assertEquals(brandList, result);
    }

    @Test
     void testCreateBrand() throws Exception {
        CreateBrandRequest request = new CreateBrandRequest();
        request.setBrandname("BrandName");
        request.setDescription("Description");
        Brand createdBrand = new Brand();
        createdBrand.setId(1L);
        createdBrand.setBrandname(request.getBrandname());
        createdBrand.setDescription(request.getDescription());

        when(brandService.createBrand(request)).thenReturn(createdBrand);
        Assertions.assertTrue(brandController.createBrand(request, 1).getBody().getSuccess());
    }

    @Test
     void testModifyBrand() throws Exception {
        ModifyBrandRequest modifyRequest = new ModifyBrandRequest(1L,"BrandA","description");
        modifyRequest.setBrandname("BrandA");
        modifyRequest.setDescription("description");
        modifyRequest.setId(1L);

        Brand brand = new Brand();
        brand.setId(1L);
        brand.setBrandname("BrandA");
        brand.setDescription("description");

        when(brandService.modifyBrand(modifyRequest)).thenReturn(brand);
        Assertions.assertTrue(brandController.modifyBrand(modifyRequest,1).getBody().getSuccess());
    }

    @Test
    void testDeleteBrand(){
        Assertions.assertTrue(brandController.deleteBrand(1L,1).getBody().getSuccess());
    }

    @Test
    void testGetBrandByBrandname(){
        Brand brand1 = new Brand();
        brand1.setBrandname("Brandname1");
        Brand brand2 = new Brand();
        brand2.setBrandname("anotherBrand");
        Brand brand3 = new Brand();
        brand3.setBrandname("yetanotherBrand");

        List<Brand> brandList = Arrays.asList(brand1, brand2, brand3);
        when(brandService.getBrandByBrandname("brandname")).thenReturn(brandList);
        Assertions.assertEquals(brandList, brandController.getBrandByBrandname("brandname").getBody());
    }
}
