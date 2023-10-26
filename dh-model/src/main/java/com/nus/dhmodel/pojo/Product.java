package com.nus.dhmodel.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;


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

//    @OneToMany
//    private List<Observer> observers = new ArrayList<>();


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

    public Product(Long id, String productname, String brandname,double currentPrice) {
        this.id = id;
        this.productname = productname;
        this.brandname = brandname;
        this.currentPrice = currentPrice;
        this.lowestPrice = currentPrice;
    }

    public Product(String productname, String brandname,double currentPrice ) {
        this.productname = productname;
        this.brandname = brandname;
        this.currentPrice = currentPrice;
        this.lowestPrice = currentPrice;
        this.watchers = new HashSet<>();
    }

    public Product(String productname, String brandname, double currentPrice,String storeAddress, String description) {
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

        for(User user : watchers){
        user.update(this, newPrice);
      }
    }

    public Product() {}
}
