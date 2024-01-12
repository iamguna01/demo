package com.tao.digital.test.demo.service;

// ApprovalQueueService.java

import com.tao.digital.test.demo.dao.ApprovalQueueRepository;
import com.tao.digital.test.demo.entity.ApprovalQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprovalQueueService {

    private final ApprovalQueueRepository approvalQueueRepository;

    @Autowired
    public ApprovalQueueService(ApprovalQueueRepository approvalQueueRepository) {
        this.approvalQueueRepository = approvalQueueRepository;
    }

    public List<ApprovalQueue> listAllApprovals() {
        return approvalQueueRepository.findAll();
    }

    public ApprovalQueue getApprovalById(Long approvalId) {
        return approvalQueueRepository.findById(approvalId)
                .orElseThrow(() -> new IllegalArgumentException("Approval not found with id: " + approvalId));
    }


}
