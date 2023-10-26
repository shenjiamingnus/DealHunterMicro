package com.nus.dhmodel.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
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


    @ManyToMany(mappedBy = "watchedProducts")
    @JsonIgnore
    private Set<User> watchers;


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

    public void addWatcher(User user) {
        watchers.add(user);
    }

    public void removeWatcher(User user) {
        watchers.remove(user);
    }

    public void notify(double newPrice){
        if (this.watchers == null) {
            this.watchers = new HashSet<>(); // 创建一个空的 watchers 集合
        }
    }

    public Product() {}
}
