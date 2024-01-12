package com.tao.digital.test.demo.entity;
// ApprovalQueueProduct.java

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_queue_product")
public class ApprovalQueueProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // This ID should match the ID of the related Product

    private String name;

    private BigDecimal price;

    private boolean status;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isStatus() {
        return status;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    @Column(name = "posted_date")
    private LocalDateTime postedDate;


}
