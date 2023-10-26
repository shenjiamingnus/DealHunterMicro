package com.nus.dhmodel.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
<<<<<<< HEAD
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.Instant;

=======
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

>>>>>>> main
@Data
@Entity
@Table(name = "price_history")
public class PriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;

    @CreatedDate
    private Instant createDate;


    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
<<<<<<< HEAD
    private Product product;
=======
    private com.nus.dealhunter.model.Product product;
>>>>>>> main

//    public PriceHistory(double price, LocalDate date, Product product) {
//        this.price = price;
//        this.date = date;
//        this.product = product;
//    }

<<<<<<< HEAD
    public PriceHistory(Long id, double price, Instant createDate, Product product) {
=======
    public PriceHistory(Long id, double price, Instant createDate, com.nus.dealhunter.model.Product product) {
>>>>>>> main
        this.id = id;
        this.price = price;
        this.createDate = createDate;
        this.product = product;
    }

<<<<<<< HEAD
    public PriceHistory(double price, Instant createDate, Product product) {
=======
    public PriceHistory(double price, Instant createDate, com.nus.dealhunter.model.Product product) {
>>>>>>> main
        this.price = price;
        this.createDate = createDate;
        this.product = product;
    }


    public PriceHistory() {
    }

}
