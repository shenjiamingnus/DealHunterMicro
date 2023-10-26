package com.nus.dhmodel.pojo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Data

@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"productname"})
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String productname;

    @NotBlank
    @Size(max = 50)
    private String brandname;


    private String storeAddress;

    private String description;

    private String imageUrl;

    private Double currentPrice;

    @JsonInclude
    private Double lowestPrice;


    @CreatedDate
    private Instant createDate;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PriceHistory> priceHistoryList = new ArrayList<>();

//    @ManyToMany(mappedBy = "watchedProducts")
//    private Set<User> watchers = new HashSet<>();



    public Product(String productname) {
        this.productname = productname;
    }


    public Product(String productname, String brandname) {
        this.productname = productname;
        this.brandname = brandname;
    }


    public Product(Long id, String productname, String brandname, double currentPrice) {

        this.id = id;
        this.productname = productname;
        this.brandname = brandname;
        this.currentPrice = currentPrice;
        this.lowestPrice = currentPrice;
    }


    public Product(String productname, String brandname, double currentPrice ) {

        this.productname = productname;
        this.brandname = brandname;
        this.currentPrice = currentPrice;
        this.lowestPrice = currentPrice;

    }

    public Product(String productname, String brandname, double currentPrice, String storeAddress, String description) {

        this.productname = productname;
        this.brandname = brandname;
        this.currentPrice = currentPrice;
        this.lowestPrice = currentPrice;
        this.storeAddress = storeAddress;
        this.description = description;
    }

    public Product(String productname, String brandname, double currentPrice, String storeAddress, String description, String imageUrl) {
        this.productname = productname;
        this.brandname = brandname;
        this.currentPrice = currentPrice;
        this.lowestPrice = currentPrice;
        this.storeAddress = storeAddress;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Product(Long productId) {
        this.id =productId;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCurrentPrice(double currentprice) {
        this.currentPrice = currentprice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setLowestPrice(double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public double getLowestPrice() {
        return lowestPrice;
    }




//    public void addWatcher(User user) {
//        watchers.add(user);
//    }
//
//    public void removeWatcher(User user) {
//        watchers.remove(user);
//    }



    public Product() {}
}
