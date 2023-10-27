package com.nus.dhbrand.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateBrandRequest {

    @NotBlank
    @Size(max = 50)
    private String brandname;

    @NotBlank
    private String description;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private Long id;

    public CreateBrandRequest(String brandname, String description, String imageUrl){
        this.brandname= brandname;
        this.description =description;
        this.imageUrl = imageUrl;
    }
}
