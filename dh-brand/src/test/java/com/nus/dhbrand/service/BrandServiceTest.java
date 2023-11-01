package com.nus.dhbrand.service;

import com.nus.dhbrand.exception.BrandServiceException;
import com.nus.dhbrand.payload.request.CreateBrandRequest;
import com.nus.dhbrand.payload.request.ModifyBrandRequest;
import com.nus.dhbrand.repository.BrandRepository;

import com.nus.dhmodel.pojo.Brand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
class BrandServiceTest {

    @Mock
    BrandRepository brandRepository;

    @InjectMocks
    BrandService brandService;

    @Test
    void testCheckBrandNameExists(){
        Mockito.when(brandRepository.existsByBrandname("A")).thenReturn(true);
        assertTrue(brandService.checkBrandNameExists("A"));
    }

    @Test
    void testGetAllBrands(){
        List<Brand> brands = Arrays.asList(new Brand(),new Brand());
        Mockito.when(brandRepository.findAll()).thenReturn(brands);

        List<Brand> result = brandService.getAllBrands();

        Assertions.assertEquals(brands, result);

        when(brandRepository.findAll()).thenThrow(NullPointerException.class);
        Assertions.assertThrows(BrandServiceException.class, ()->{
            brandService.getAllBrands();
        });
    }

    @Test
    void testCreateBrand(){
        CreateBrandRequest createBrandRequest = new CreateBrandRequest();
        createBrandRequest.setBrandname("BrandName");
        createBrandRequest.setDescription("Description");

        Brand brand = new Brand();
        brand.setBrandname(createBrandRequest.getBrandname());
        brand.setDescription(createBrandRequest.getDescription());
        brand.setCreateDate((Instant.now()));

        // 模拟BrandRepository的save方法，使其返回模拟的Brand对象
        when(brandRepository.save(ArgumentMatchers.any(Brand.class))).thenReturn(brand);

        // 执行被测试的方法
        Brand createdBrand = brandService.createBrand(createBrandRequest);

        // 断言返回的Brand对象是否符合预期
        Assertions.assertEquals("BrandName", createdBrand.getBrandname());
        Assertions.assertEquals("Description", createdBrand.getDescription());

        Assertions.assertNull(brandService.createBrand(null));
    }

    @Test
    void testModifyBrand() {
        // 创建一个模拟的ModifyBrandRequest
        ModifyBrandRequest modifyRequest = new ModifyBrandRequest(1L,"NewBrandName","NewBrandDescription");

        // 创建一个模拟的Brand对象
        Brand brand = new Brand();
        brand.setId(1L); // 设置与请求中相同的ID
        brand.setBrandname("OldBrandName");
        brand.setDescription("OldBrandDescription");

        // 使用Mockito模拟BrandRepository的findById和save方法
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));
        when(brandRepository.save(brand)).thenReturn(brand);

        // 执行被测试的方法
        Brand modifiedBrand = brandService.modifyBrand(modifyRequest);

        // 验证模拟的BrandRepository的findById和save方法是否被调用
        verify(brandRepository).findById(1L);
        verify(brandRepository).save(brand);

        // 验证修改后的品牌属性是否符合预期
        Assertions.assertEquals("NewBrandName", modifiedBrand.getBrandname());
        Assertions.assertEquals("NewBrandDescription", modifiedBrand.getDescription());
    }

    @Test
    void testDeleteBrand() {
        // 设置要删除的品牌的ID
        Long brandIdToDelete = 1L;

        // 使用Mockito模拟brandRepository的deleteById方法
        doNothing().when(brandRepository).deleteById(brandIdToDelete);

        // 执行被测试的方法
        assertDoesNotThrow(() -> brandService.deleteBrand(brandIdToDelete));

        // 验证brandRepository的deleteById方法是否被调用
        verify(brandRepository).deleteById(brandIdToDelete);

    }

    @Test
    void testGetBrandByBrandname() {
        // 设置要查找的品牌名称
        String brandnameToFind = "TestBrand";

        // 创建一个模拟的品牌列表，用于模拟数据库返回的结果
        List<Brand> mockBrandList = new ArrayList<>();

        Brand brand1 = new Brand();
        brand1.setBrandname("Brandname1");
        Brand brand2 = new Brand();
        brand2.setBrandname("anotherBrand");
        Brand brand3 = new Brand();
        brand3.setBrandname("yetanotherBrand");

        mockBrandList.add(brand1);
        mockBrandList.add(brand2);
        mockBrandList.add(brand3);

        // 使用Mockito模拟brandRepository的findByBrandname方法
        when(brandRepository.findByBrandname(brandnameToFind)).thenReturn(mockBrandList);

        // 执行被测试的方法
        List<Brand> resultBrands = brandService.getBrandByBrandname(brandnameToFind);

        // 验证返回的品牌列表是否与模拟的列表匹配
        Assertions.assertEquals(mockBrandList.size(), resultBrands.size());

        when(brandRepository.findByBrandname("A")).thenThrow(NullPointerException.class);
        Assertions.assertThrows(BrandServiceException.class, ()->{
          brandService.getBrandByBrandname("A");
        });

    }

}
