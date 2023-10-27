package com.nus.dhmodel.pojo;

import java.util.Set;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"productname"})
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String productName;

    @NotBlank
    @Size(max = 50)
    private String brandName;

    private String storeAddress;

    private String description;

    private String imageUrl;

    private Double currentPrice;

    private Double lowestPrice;

    @CreatedDate
    private Instant createDate;

    private Long brandId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PriceHistory> priceHistoryList = new ArrayList<>();

    @ElementCollection
    private Set<Long> watcherUserId;

    public Product() {}
}
