package com.tao.digital.test.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ApprovalQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ApprovalQueueProduct approvalQueueProduct;

    @Column(name = "request_date")
    private LocalDateTime requestDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApprovalQueueProduct getApprovalQueueProduct() {
        return approvalQueueProduct;
    }

    public void setApprovalQueueProduct(ApprovalQueueProduct product) {
        this.approvalQueueProduct = product;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
}
