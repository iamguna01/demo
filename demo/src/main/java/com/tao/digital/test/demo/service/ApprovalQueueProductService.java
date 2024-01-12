package com.tao.digital.test.demo.service;

// ApprovalQueueProductService.java

import com.tao.digital.test.demo.dao.ApprovalQueueProductRepository;
import com.tao.digital.test.demo.entity.ApprovalQueueProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApprovalQueueProductService {

    private final ApprovalQueueProductRepository approvalQueueProductRepository;

    @Autowired
    public ApprovalQueueProductService(ApprovalQueueProductRepository approvalQueueProductRepository) {
        this.approvalQueueProductRepository = approvalQueueProductRepository;
    }

    public ApprovalQueueProduct getApprovalQueueProductById(Long productId) {
        return approvalQueueProductRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("ApprovalQueueProduct not found with id: " + productId));
    }

    // Additional methods as needed
}
