package com.nus.dhmodel.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;


@Data
@Entity
@Table(name = "brands", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"brandname"})
})
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String brandname;

    private String description;

    private String imageUrl;

    @CreatedDate
    private Instant createDate;


    @OneToMany(mappedBy = "brand")
    @JsonIgnore
    private List<Product> products;

    public Brand(String brandname) {
        this.brandname = brandname;
    }

    public Brand(Long id, String brandname){
        this.id = id;
        this.brandname = brandname;
    }


    public Brand(String brandname, String description, Long id) {
        this.brandname = brandname;
        this.id = id;
        this.description = description;
    }

    public Brand(){}

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname){
        this.brandname = brandname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



}